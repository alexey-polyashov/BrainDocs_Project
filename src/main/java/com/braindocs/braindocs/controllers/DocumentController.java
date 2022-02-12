package com.braindocs.braindocs.controllers;

import com.braindocs.braindocs.DTO.FieldsListDTO;
import com.braindocs.braindocs.DTO.SearchCriteriaDTO;
import com.braindocs.braindocs.DTO.SearchCriteriaListDTO;
import com.braindocs.braindocs.DTO.documents.DocumentDTO;
import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.files.NewFileDTOwithData;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.files.FileModel;
import com.braindocs.braindocs.repositories.specifications.DocumentSpecificationBuilder;
import com.braindocs.braindocs.services.DocumentTypeService;
import com.braindocs.braindocs.services.DocumentsService;
import com.braindocs.braindocs.services.FilesService;
import com.braindocs.braindocs.services.UserService;
import com.braindocs.braindocs.services.mappers.DocumentMapper;
import com.braindocs.braindocs.services.mappers.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @GetMapping(value="/get_fileds")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Вид документа", "documentType", "DocumentTypeModel", new HashSet<String>(Arrays.asList(":")), "Long"));
        fieldsSet.add(new FieldsListDTO("Номер докуемнта","number", "", new HashSet<String>(Arrays.asList(":")), "String"));
        fieldsSet.add(new FieldsListDTO("Дата документа","documentDate","", new HashSet<String>(Arrays.asList("<",">")), "Date"));
        fieldsSet.add(new FieldsListDTO("Заголовок документа", "heading","", new HashSet<String>(Arrays.asList(":")), "String"));
        fieldsSet.add(new FieldsListDTO("Содержание", "content","", new HashSet<String>(Arrays.asList(":")), "String"));
        fieldsSet.add(new FieldsListDTO("Автор документа", "author","UserModel", new HashSet<String>(Arrays.asList(":")), "Long"));
        fieldsSet.add(new FieldsListDTO("Ответственный за документ", "responsible","UserModel", new HashSet<String>(Arrays.asList(":")), "Long"));
        fieldsSet.add(new FieldsListDTO("Организация", "organisation","OrganisationModel", new HashSet<String>(Arrays.asList(":")), "Long"));
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
    public void deleteDocument(@PathVariable Long id){
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
    public DocumentDTO getDocumentById(@PathVariable Long id){
        log.info("DocumentController: getDocumentById");
        DocumentModel doc = documentsService.getDocumentById(id);
        log.info("DocumentController: getDocumentById - ok");
        return documentMapper.toDTO(doc);
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

    @PostMapping(value="/upload_files/{docid}")
    public Long uploadFiles(@PathVariable Long docid, @RequestBody List<NewFileDTOwithData> files){
        log.info("DocumentController: uploadfiles");
        return null;
    }

    @PostMapping(value="/upload_file/{docid}")
    public Long uploadFile(@PathVariable Long docId, @RequestBody NewFileDTOwithData file){
        log.info("DocumentController: uploadfile");
        MultipartFile fileData = file.getFileData();
        if(fileData.isEmpty()){
            log.info("file is empty");
            throw new RuntimeException("file is empty");
        }
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(file);
        } catch (IOException e) {
            log.error("get file data error\n" + e.getMessage() + "\n" + e.getCause());
            throw new RuntimeException("get file data error");
        }
        documentsService.addFile(docId, fileModel);
        return null;
    }

    @GetMapping(value="/get_files/{docid}")
    public List<FileDTO> getFiles(@PathVariable Long docid){
        log.info("DocumentController: getfileslist");
        return null;
    }

    @GetMapping(value="/getfile_data/{docid}/{fileid}")
    public List<FileDTO> getFileData(@PathVariable Long docid, Long fileid){
        log.info("DocumentController: getfileslist");
        return null;
    }

}
