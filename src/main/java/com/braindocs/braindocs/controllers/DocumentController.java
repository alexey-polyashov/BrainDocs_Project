package com.braindocs.braindocs.controllers;

import com.braindocs.braindocs.DTO.FieldsListDTO;
import com.braindocs.braindocs.DTO.SearchCriteriaDTO;
import com.braindocs.braindocs.DTO.SearchCriteriaListDTO;
import com.braindocs.braindocs.DTO.documents.DocumentDTO;
import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.files.FileDataDTO;
import com.braindocs.braindocs.DTO.files.NewFileDTO;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.files.FileModel;
import com.braindocs.braindocs.repositories.specifications.DocumentSpecificationBuilder;
import com.braindocs.braindocs.services.DocumentTypeService;
import com.braindocs.braindocs.services.DocumentsService;
import com.braindocs.braindocs.services.UserService;
import com.braindocs.braindocs.services.mappers.DocumentMapper;
import com.braindocs.braindocs.services.mappers.FileMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/documents/")
@Slf4j
public class DocumentController {

    private final DocumentsService documentsService;
    private final DocumentTypeService documentTypeService;
    private final UserService userService;
    private final DocumentMapper documentMapper;
    private final FileMapper fileMapper;

    private void setLinkToFile(FileDTO fileDTO, Long docId){
        fileDTO.setLink("/api/v1/documents/get_file_data/" + docId + "/" + fileDTO.getId());
    }

    @GetMapping(value="/get_fileds")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Вид документа", "documentType", "DocumentTypeModel", new HashSet<String>(Arrays.asList(":")), "Long", false));
        fieldsSet.add(new FieldsListDTO("Номер докуемнта","number", "", new HashSet<String>(Arrays.asList(":")), "String", false));
        fieldsSet.add(new FieldsListDTO("Дата документа","documentDate","", new HashSet<String>(Arrays.asList("<",">")), "Date", true));
        fieldsSet.add(new FieldsListDTO("Заголовок документа", "heading","", new HashSet<String>(Arrays.asList(":")), "String", false));
        fieldsSet.add(new FieldsListDTO("Содержание", "content","", new HashSet<String>(Arrays.asList(":")), "String", true));
        fieldsSet.add(new FieldsListDTO("Автор документа", "author","UserModel", new HashSet<String>(Arrays.asList(":")), "Long", true));
        fieldsSet.add(new FieldsListDTO("Ответственный за документ", "responsible","UserModel", new HashSet<String>(Arrays.asList(":")), "Long", false));
        fieldsSet.add(new FieldsListDTO("Организация", "organisation","OrganisationModel", new HashSet<String>(Arrays.asList(":")), "Long", true));
        log.info("DocumentController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @PostMapping(value="/add")
    public Long addDocument(@Valid @RequestBody DocumentDTO documentDTO) throws ParseException {
        log.info("DocumentController: add");
        DocumentModel docModel = documentMapper.toModel(documentDTO);
        Long docId = documentsService.addDocument(docModel);
        log.info("DocumentController: add (return id {})", docId);
        return docId;
    }

    @PostMapping(value="/save")
    public Long saveDocument(@Valid @RequestBody DocumentDTO documentDTO) throws ParseException {
        log.info("DocumentController: saveDocument");
        DocumentModel docModel = documentMapper.toModel(documentDTO);
        Long docId = documentsService.saveDocument(docModel);
        log.info("DocumentController: add (saveDocument id {})", docId);
        return docId;
    }

    @DeleteMapping(value="{id}")
    public void deleteDocument(@PathVariable("id") Long id){
        log.info("DocumentController: deleteDocument, id-{}", id);
        documentsService.deleteDocument(id);
        log.info("DocumentController: deleteDocument - ok");
    }

    @GetMapping(value="/")
    public Page<DocumentDTO> getDocuments(@RequestParam( name = "pagenumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pagesize", defaultValue = "10") int pageSize){
        log.info("DocumentController: getDocuments, pagenumber-{}, pagesize-{}", pageNumber, pageSize);
        Page<DocumentModel> documents = documentsService.getDocuments(pageNumber, pageSize);
        Page<DocumentDTO> docDtoPage = documents.map(documentMapper::toDTO);
        log.info("DocumentController: getDocuments return {} elements", docDtoPage.getNumberOfElements());
        return docDtoPage;
    }

    @GetMapping(value="{id}")
    public DocumentDTO getDocumentById(@PathVariable("id") Long id){
        log.info("DocumentController: getDocumentById");
        DocumentModel doc = documentsService.getDocumentById(id);
        DocumentDTO docDTO = documentMapper.toDTO(doc);
        if(docDTO.getFiles()!=null) {
            for (FileDTO fileDTO : docDTO.getFiles()) {
                setLinkToFile(fileDTO, id);
            }
        }
        log.info("DocumentController: getDocumentById - ok");
        return docDTO;
    }

    @PostMapping(value="/get_list")
    public Page<DocumentDTO>  getDocumentsByFilter(@RequestBody SearchCriteriaListDTO requestDTO){
        log.info("DocumentController: getDocumentsByFilter");
        List<SearchCriteriaDTO> filter = requestDTO.getFilter();
        Integer page = requestDTO.getPage();
        Integer recordsOnPage = requestDTO.getRecordsOnPage();
        DocumentSpecificationBuilder builder = new DocumentSpecificationBuilder(userService);
        for(SearchCriteriaDTO creteriaDTO: filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<DocumentModel> spec = builder.build();
        Page<DocumentModel> documents = documentsService.getDocumentsByFields(page, recordsOnPage, spec);
        Page<DocumentDTO> docDtoPage = documents.map(documentMapper::toDTO);
        log.info("DocumentController: getDocumentsByFilter return {} elements", docDtoPage.getSize());
        return docDtoPage;
    }


    //@PostMapping(value="/upload_file/{docid}")
    @RequestMapping(value = "/upload_file/{docid}", method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    public FileDTO uploadFile(@PathVariable("docid") Long docid, @RequestPart("fileDescribe") String jsonDescribe, @RequestPart("file") MultipartFile fileData) throws IOException {
        log.info("DocumentController: uploadfile");
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        if(fileData.isEmpty()){
            log.info("file is empty");
            throw new RuntimeException("file is empty");
        }
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("get file data error\n" + e.getMessage() + "\n" + e.getCause());
            throw new RuntimeException("get file data error");
        }
        fileModel = documentsService.addFile(docid, fileModel, fileData);
        FileDTO fileDTO = fileMapper.toDTO(fileModel);
        setLinkToFile(fileDTO, docid);
        return fileDTO;
    }

    @GetMapping(value="/get_files_list/{docid}")
    public Set<FileDTO> getFiles(@PathVariable("docid") Long docid){
        log.info("DocumentController: getFileslist");
        Set<FileModel> filesList = documentsService.getFilesList(docid);
        Set<FileDTO> fileDTOs = filesList.stream().map(
                (p)->{
                    FileDTO res = fileMapper.toDTO(p);
                    setLinkToFile(res, docid);
                    return res;
                }
            ).collect(Collectors.toSet());
        return fileDTOs;
    }

    @GetMapping(value="/get_file_describe/{docid}/{fileId}")
    public FileDTO getFileDescribe(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileDescribe, docid-{}, fileid{}", docid, fileid);
        FileDTO fDTO = fileMapper.toDTO(documentsService.getFileDescribe(docid, fileid));
        setLinkToFile(fDTO, docid);
        return fDTO;
    }

    @GetMapping(value="/get_file_data/{docid}/{fileid}")
    @ResponseBody
    public ResponseEntity<byte[]> getFileData(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileData");
        FileDataDTO fileData = documentsService.getFileData(docid, fileid);
        MediaType mt = MediaType.valueOf(fileData.getContentType());
        return ResponseEntity.ok().contentType(mt).body(fileData.getFileData());
    }

    @RequestMapping(value = "/change_file/{docid}", method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    public FileDTO changeFile(@PathVariable("docid") Long docid, @RequestPart("fileDescribe") String jsonDescribe, @RequestPart("file") MultipartFile fileData) throws JsonProcessingException {
        log.info("DocumentController: changeFile, docid-{}, fileDescribe{}", docid, jsonDescribe);
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("get file data error\n{}\n{}", e.getMessage(), e.getCause());
            throw new RuntimeException("get file data error");
        }
        fileModel = documentsService.changeFile(docid, fileModel, fileData);
        FileDTO fileDTO = fileMapper.toDTO(fileModel);
        setLinkToFile(fileDTO, docid);
        log.info("DocumentController: changeFile - changed, docid-{}, fileDescribe - {}, fileId-{}", docid, fileDTO.getId(), jsonDescribe);
        return fileDTO;
    }

    @DeleteMapping(value="/delete_file/{docid}/{fileid}")
    public void deleteFile(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: deleteFile, docid-{}, fileid{}", docid, fileid);
        documentsService.deleteFile(docid, fileid);
        log.info("DocumentController: deleteFile - deleted, docid-{}, fileid{}", docid, fileid);
    }

    @DeleteMapping(value="/clear_file/{docid}")
    public void clearFiles(@PathVariable("docid") Long docid){
        log.info("DocumentController: clearFiles, docid-{}", docid);
        documentsService.clearFiles(docid);
        log.info("DocumentController: clearFiles - done, docid-{}", docid);
    }

}
