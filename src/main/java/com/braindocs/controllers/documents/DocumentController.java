package com.braindocs.controllers.documents;

import com.braindocs.dto.FieldsListDTO;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.SearchCriteriaListDTO;
import com.braindocs.dto.documents.DocumentDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.dto.files.NewFileDTO;
import com.braindocs.exceptions.AnyOtherException;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.repositories.specifications.DocumentSpecificationBuilder;
import com.braindocs.services.documents.DocumentTypeService;
import com.braindocs.services.documents.DocumentsService;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.UserService;
import com.braindocs.services.mappers.DocumentMapper;
import com.braindocs.services.mappers.FileMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
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
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/documents")
@Slf4j
@Api(value = "DocumentController", tags = "Контролер документов")
public class DocumentController {

    private final DocumentsService documentsService;
    private final DocumentTypeService documentTypeService;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final DocumentMapper documentMapper;
    private final FileMapper fileMapper;

    private static final String STRING_TYPE = "String";
    private static final String LONG_TYPE = "Long";
    private static final String DATE_TYPE = "Date";

    private void setLinkToFile(FileDTO fileDTO, Long docId){
        fileDTO.setLink("/api/v1/documents/" + docId + "/files/" + fileDTO.getId() + "/data");
    }

    @GetMapping(value="/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Вид документа", "documentType", "/api/v1/documents/types", Arrays.asList(":"), LONG_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Номер докуемнта","number", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Дата документа","documentDate","", Arrays.asList("<",">"), DATE_TYPE, true));
        fieldsSet.add(new FieldsListDTO("Заголовок документа", "heading","", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Содержание", "content","", Arrays.asList(":"), STRING_TYPE, true));
        fieldsSet.add(new FieldsListDTO("Автор документа", "author","/api/v1/users", Arrays.asList(":"), LONG_TYPE, true));
        fieldsSet.add(new FieldsListDTO("Ответственный за документ", "responsible","/api/v1/users", Arrays.asList(":"), LONG_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Организация", "organisation","/api/v1//organisations", Arrays.asList(":"), LONG_TYPE, true));
        log.info("DocumentController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @PostMapping(value="")
    public Long addDocument(@Valid @RequestBody DocumentDTO documentDTO) throws ParseException {
        log.info("DocumentController: add");
        if(documentDTO.getId()!=null && (documentDTO.getId()!=0)){
            throw new AnyOtherException("При добавлении нового объекта id должен быть пустым");
        }
        DocumentModel docModel = documentMapper.toModel(documentDTO);
        Long docId = documentsService.addDocument(docModel);
        log.info("DocumentController: add (return id {})", docId);
        return docId;
    }

    @PostMapping(value="/{id}")
    public Long saveDocument(@PathVariable("id") Long id, @Valid @RequestBody DocumentDTO documentDTO) throws ParseException {
        log.info("DocumentController: saveDocument");
        if(id==0){
            throw new AnyOtherException("id не должен быть пустым");
        }
        DocumentModel docModel = documentMapper.toModel(documentDTO);
        docModel.setId(id);
        Long docId = documentsService.saveDocument(docModel);
        log.info("DocumentController: add (saveDocument id {})", docId);
        return docId;
    }

    @DeleteMapping(value="/finally/{id}")
    public void deleteDocument(@PathVariable("id") Long id){
        log.info("DocumentController: deleteDocument, id-{}", id);
        documentsService.deleteDocument(id);
        log.info("DocumentController: deleteDocument - ok");
    }

    @DeleteMapping(value="/{id}")
    public void markDocument(@PathVariable("id") Long id){
        log.info("DocumentController: markDocument, id-{}", id);
        documentsService.markDocument(id);
        log.info("DocumentController: markDocument - ok");
    }

    @GetMapping(value="")
    public Page<DocumentDTO> getDocuments(@RequestParam( name = "pagenumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pagesize", defaultValue = "10") int pageSize){
        log.info("DocumentController: getDocuments, pagenumber-{}, pagesize-{}", pageNumber, pageSize);
        Page<DocumentDTO> docDtoPage = documentsService.getDocumentsDTO(pageNumber,
                pageSize,
                this::setLinkToFile);
        log.info("DocumentController: getDocuments return {} elements", docDtoPage.getNumberOfElements());
        return docDtoPage;
    }

    @GetMapping(value="{id}")
    public DocumentDTO getDocumentById(@PathVariable("id") Long id){
        log.info("DocumentController: getDocumentById");
        DocumentDTO docDTO = documentsService.getDocumentDTOById(id);
        if(docDTO.getFiles()!=null) {
            for (FileDTO fileDTO : docDTO.getFiles()) {
                setLinkToFile(fileDTO, id);
            }
        }
        log.info("DocumentController: getDocumentById - ok");
        return docDTO;
    }

    @PostMapping(value="/search")
    public Page<DocumentDTO>  getDocumentsByFilter(@RequestBody SearchCriteriaListDTO requestDTO){
        log.info("DocumentController: getDocumentsByFilter");
        List<SearchCriteriaDTO> filter = requestDTO.getFilter();
        Integer page = requestDTO.getPage();
        Integer recordsOnPage = requestDTO.getRecordsOnPage();
        DocumentSpecificationBuilder builder = new DocumentSpecificationBuilder(userService, organisationService, documentTypeService);
        for(SearchCriteriaDTO creteriaDTO: filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<DocumentModel> spec = builder.build();
        Page<DocumentDTO> docDtoPage = documentsService.getDocumentsDTOByFields(page, recordsOnPage, spec);
        log.info("DocumentController: getDocumentsByFilter return {} elements", docDtoPage.getSize());
        return docDtoPage;
    }

    @PostMapping(value = "/{docid}/files/upload",
            consumes = {"multipart/form-data"})
    public FileDTO uploadFile(@PathVariable("docid") Long docid, @RequestPart("fileDescribe") String jsonDescribe, @RequestPart("file") MultipartFile fileData) throws IOException {
        log.info("DocumentController: uploadfile");
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        if(fileData.isEmpty()){
            log.info("Нет данных файла");
            throw new AnyOtherException("Нет данных файла");
        }
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("Ошибка получения данных файла\n" + e.getMessage() + "\n" + e.getCause());
            throw new AnyOtherException("Ошибка получения данных файла");
        }
        FileDTO fileDTO = documentsService.addFile(docid, fileModel, fileData);
        setLinkToFile(fileDTO, docid);
        return fileDTO;
    }

    @PostMapping(value = "/{docid}/files/{fileid}",
            consumes = {"multipart/form-data"})
    public FileDTO changeFile(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileId, @RequestPart("fileDescribe") String jsonDescribe, @RequestPart("file") MultipartFile fileData) throws IOException {
        log.info("DocumentController: changeFile, docid-{}, fileDescribe{}", docid, jsonDescribe);
        if(fileId==0){
            throw new AnyOtherException("id файла не должен быть пустым");
        }
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("Ошибка получения данных файла\n{}\n{}", e.getMessage(), e.getCause());
            throw new AnyOtherException("Ошибка получения данных файла");
        }
        fileModel.setId(fileId);
        FileDTO fileDTO = documentsService.changeFile(docid, fileModel, fileData);
        setLinkToFile(fileDTO, docid);
        log.info("DocumentController: changeFile - changed, docid-{}, fileDescribe - {}, fileId-{}", docid, fileDTO.getId(), jsonDescribe);
        return fileDTO;
    }

    @GetMapping(value="/{docid}/files/{fileid}/data",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getFileData(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileData");
        FileDataDTO fileData = documentsService.getFileData(docid, fileid);
        MediaType mt = MediaType.valueOf(fileData.getContentType());
        return ResponseEntity.ok().contentType(mt).body(fileData.getFileData());
    }

    @GetMapping(value="/{docid}/files")
    public Set<FileDTO> getFiles(@PathVariable("docid") Long docid){
        log.info("DocumentController: getFileslist");
        return documentsService.getFilesDTOList(docid, this::setLinkToFile);
    }

    @GetMapping(value="/{docid}/files/{fileid}")
    public FileDTO getFileDescribe(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileDescribe, docid-{}, fileid{}", docid, fileid);
        return documentsService.getFileDTODescribe(
                docid, fileid, this::setLinkToFile);
    }

    @DeleteMapping(value="/{docid}/files/{fileid}")
    public void deleteFile(@PathVariable("docid") Long docid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: deleteFile, docid-{}, fileid{}", docid, fileid);
        documentsService.deleteFile(docid, fileid);
        log.info("DocumentController: deleteFile - deleted, docid-{}, fileid{}", docid, fileid);
    }

    @DeleteMapping(value="/{docid}/files/clear")
    public void clearFiles(@PathVariable("docid") Long docid){
        log.info("DocumentController: clearFiles, docid-{}", docid);
        documentsService.clearFiles(docid);
        log.info("DocumentController: clearFiles - done, docid-{}", docid);
    }

}
