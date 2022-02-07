package com.braindocs.braindocs.controllers;

import com.braindocs.braindocs.DTO.SearchCriteriaDTO;
import com.braindocs.braindocs.DTO.SearchCriteriaListDTO;
import com.braindocs.braindocs.DTO.documents.DocumentDTO;
import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.files.FileDTOwithData;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.repositories.specifications.DocumentSpecificationBuilder;
import com.braindocs.braindocs.services.DocumentTypeService;
import com.braindocs.braindocs.services.DocumentsService;
import com.braindocs.braindocs.services.UserService;
import com.braindocs.braindocs.services.mappers.DocumentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/21/documents/")
@Slf4j
public class DocumentController {

    private final DocumentsService documentsService;
    private final DocumentTypeService documentTypeService;
    private final UserService userService;
    private final DocumentMapper documentMapper;

    @PostMapping(value="/add")
    public Long addDocument(@Valid DocumentDTO documentDTO){
        log.info("DocumentController: add");
        DocumentModel docModel = documentMapper.toModel(documentDTO);
        Long docId = documentsService.addDocument(docModel);
        log.info("DocumentController: add (return id {})", docId);
        return docId;
    }

    @PostMapping(value="/save")
    public Long saveDocument(@Valid DocumentDTO documentDTO){
        log.info("DocumentController: saveDocument");
        DocumentModel docModel = documentMapper.toModel(documentDTO);
        Long docId = documentsService.saveDocument(docModel);
        log.info("DocumentController: add (saveDocument id {})", docId);
        return docId;
    }

    @DeleteMapping(value="{id}")
    public void deleteDocument(@PathVariable Long id){
        log.info("DocumentController: deleteDocument");
        documentsService.deleteDocument(id);
        log.info("DocumentController: deleteDocument - ok");
    }

    @GetMapping(value="/")
    public Page<DocumentDTO> getDocuments(int pageNamber, int pageSize){
        log.info("DocumentController: getDocuments");
        Page<DocumentModel> documents = documentsService.getDocuments(pageNamber, pageSize);
        Page<DocumentDTO> docDtoPage = documents.map(documentMapper::toDTO);
        log.info("DocumentController: getDocuments return {} elements", docDtoPage.getSize());
        return docDtoPage;
    }

    @GetMapping(value="{id}")
    public DocumentDTO getDocumentById(@PathVariable Long id){
        log.info("DocumentController: getDocumentById");
        DocumentModel doc = documentsService.getDocumentById(id);
        log.info("DocumentController: getDocumentById - ok");
        return documentMapper.toDTO(doc);
    }

    @GetMapping(value="/getlist")
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

    @PostMapping(value="/uploadfiles/{docid}")
    public Long uploadFiles(@PathVariable Long docid, @RequestBody List<FileDTOwithData> files){
        log.info("DocumentController: uploadfiles");
        return null;
    }

    @PostMapping(value="/uploadfile/{docid}")
    public Long uploadFile(@PathVariable Long docid, @RequestBody FileDTOwithData file){
        log.info("DocumentController: uploadfiles");
        return null;
    }

    @GetMapping(value="/getfiles/{docid}")
    public List<FileDTO> getFiles(@PathVariable Long docid){
        log.info("DocumentController: getfileslist");
        return null;
    }

    @GetMapping(value="/getfiledata/{docid}/{fileid}")
    public List<FileDTO> getFileData(@PathVariable Long docid, Long fileid){
        log.info("DocumentController: getfileslist");
        return null;
    }

}
