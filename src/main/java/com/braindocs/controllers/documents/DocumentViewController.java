package com.braindocs.controllers.documents;


import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Utils;
import com.braindocs.dto.FieldsListDTO;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.SearchCriteriaListDTO;
import com.braindocs.dto.documents.DocumentTypeDTO;
import com.braindocs.dto.documents.NewDocumentTypeDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.dto.files.NewFileDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ServiceError;
import com.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.services.documents.DocumentTypeService;
import com.braindocs.services.mappers.DocumentTypeMapper;
import com.braindocs.services.mappers.FileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents/types")
@Slf4j
public class DocumentViewController {

    private final DocumentTypeService documentTypeService;
    private final DocumentTypeMapper documentTypeMapper;
    private final FileMapper fileMapper;

    private static final String STRING_TYPE = "String";

    private void setLinkToFile(FileDTO fileDTO, Long typeId){
        fileDTO.setLink("/api/v1/documents/types/" + typeId + "/files/" + fileDTO.getId() + "/data");
    }

    @GetMapping(value="/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentViewController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Наименование вида документа", "name", "", Arrays.asList(":"), STRING_TYPE, false));
        log.info("DocumentController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @PostMapping
    public Long addView(@RequestBody NewDocumentTypeDTO documentTypeDTO) {
        log.info("DocumentViewController: addView");
        DocumentTypeModel docType = documentTypeMapper.toModel(documentTypeDTO);
        return documentTypeService.addType(docType);
    }

    @PostMapping("/{typeid}")
    public Long changeView(@PathVariable Long typeid, @RequestBody NewDocumentTypeDTO documentTypeDTO) {
        log.info("DocumentViewController: changeView");
        if(typeid==0){
            throw new BadRequestException("id должен быть отличен от 0");
        }
        return documentTypeService.changeDTOType(typeid, documentTypeMapper.toModel(documentTypeDTO));
    }

    @GetMapping("/{id}")
    public DocumentTypeDTO findById(@PathVariable Long id){
        log.info("DocumentViewController: findById");
        return documentTypeService.findDTOById(id, this::setLinkToFile);
    }

    @DeleteMapping("/finally/{id}")
    public void deleteView(@PathVariable Long id){
        log.info("DocumentViewController: deleteView");
        documentTypeService.deleteById(id);
    }

    @DeleteMapping("/{id}")
    public void markView(@PathVariable Long id){
        log.info("DocumentViewController: markView");
        documentTypeService.setMark(id, true);
    }

    @PostMapping("/unmark/{id}")
    public void unMarkView(@PathVariable Long id){
        log.info("DocumentViewController: markView");
        documentTypeService.setMark(id, false);
    }

    @GetMapping("")
    public List<DocumentTypeDTO> findAll(@RequestParam(name = "marked", defaultValue = "off", required = false) String marked) {
        log.info("DocumentViewController: findAll");
        if(!Utils.isValidEnum(MarkedRequestValue.class, marked.toUpperCase(Locale.ROOT))){
            throw new BadRequestException("Недопустимое значение параметра marked");
        }
        return documentTypeService.findAllDTO(MarkedRequestValue.valueOf(marked.toUpperCase(Locale.ROOT)),
                this::setLinkToFile);
    }

    @PostMapping("/search")
    public Page<DocumentTypeDTO> search(@RequestBody SearchCriteriaListDTO requestDTO) {
        log.info("DocumentViewController: search");
        List<SearchCriteriaDTO> filter = requestDTO.getFilter();
        Integer page = requestDTO.getPage();
        Integer recordsOnPage = requestDTO.getRecordsOnPage();
        Page<DocumentTypeDTO> docTypeDtoPage = documentTypeService.getTypesDTOByFields(
                page,
                recordsOnPage,
                filter);
        log.info("DocumentViewController: search return {} elements", docTypeDtoPage.getSize());
        return docTypeDtoPage;
    }

    @PostMapping(value = "/{typeid}/files/upload",
            consumes = {"multipart/form-data"})
    public FileDTO uploadFile(@PathVariable("typeid") Long typeid, @RequestPart("fileDescribe") String jsonDescribe, @RequestPart("file") MultipartFile fileData) throws IOException {
        log.info("DocumentController: uploadfile");
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        if(fileData.isEmpty()){
            log.info("Нет данных файла");
            throw new BadRequestException("Нет данных файла");
        }
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("Ошибка получения данных файла\n" + e.getMessage() + "\n" + e.getCause());
            throw new ServiceError("Ошибка получения данных файла");
        }
        FileDTO fileDTO = documentTypeService.addFile(typeid, fileModel, fileData);
        setLinkToFile(fileDTO, typeid);
        return fileDTO;
    }

    @PostMapping(value = "/{typeid}/files/{fileid}",
            consumes = {"multipart/form-data"})
    public FileDTO changeFile(@PathVariable("typeid") Long typeid, @PathVariable("fileid") Long fileId, @RequestPart("fileDescribe") String jsonDescribe, @RequestPart("file") MultipartFile fileData) throws IOException {
        log.info("DocumentController: changeFile, docid-{}, fileDescribe{}", typeid, jsonDescribe);
        if(fileId==0){
            throw new BadRequestException("id файла не должен быть пустым");
        }
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("Ошибка получения данных файла\n{}\n{}", e.getMessage(), e.getCause());
            throw new ServiceError("Ошибка получения данных файла");
        }
        fileModel.setId(fileId);
        FileDTO fileDTO = documentTypeService.changeFile(typeid, fileModel, fileData);
        setLinkToFile(fileDTO, typeid);
        log.info("DocumentController: changeFile - changed, docid-{}, fileDescribe - {}, fileId-{}", typeid, fileDTO.getId(), jsonDescribe);
        return fileDTO;
    }

    @GetMapping(value="/{typeid}/files/{fileid}/data")
    @ResponseBody
    public ResponseEntity<byte[]> getFileData(@PathVariable("typeid") Long typeid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileData");
        FileDataDTO fileData = documentTypeService.getFileData(typeid, fileid);
        MediaType mt = MediaType.valueOf(fileData.getContentType());
        return ResponseEntity.ok().contentType(mt).body(fileData.getFileData());
    }

    @GetMapping(value="/{typeid}/files/{fileid}/download/{filename}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getFileDataForDownload(@PathVariable("typeid") Long typeid, @PathVariable("filename") String filename, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileDataForDownload");
        FileDataDTO fileData = documentTypeService.getFileData(typeid, fileid);
        MediaType mt = MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.set("Content-Disposition", "attachment; filename=" + filename);
        return ResponseEntity.ok().headers(httpHeaders).body(fileData.getFileData());
    }

    @GetMapping(value="/{typeid}/files")
    public Set<FileDTO> getFiles(@PathVariable("typeid") Long typeid){
        log.info("DocumentController: getFileslist");
        return documentTypeService.getFilesDTOList(typeid, this::setLinkToFile);
    }

    @GetMapping(value="/{typeid}/files/{fileid}")
    public FileDTO getFileDescribe(@PathVariable("docid") Long typeid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: getFileDescribe, docid-{}, fileid{}", typeid, fileid);
        return documentTypeService.getFileDTODescribe(
                typeid, fileid, this::setLinkToFile);
    }

    @DeleteMapping(value="/{typeid}/files/{fileid}")
    public void deleteFile(@PathVariable("docid") Long typeid, @PathVariable("fileid") Long fileid){
        log.info("DocumentController: deleteFile, docid-{}, fileid{}", typeid, fileid);
        documentTypeService.deleteFile(typeid, fileid);
        log.info("DocumentController: deleteFile - deleted, docid-{}, fileid{}", typeid, fileid);
    }

    @DeleteMapping(value="/{typeid}/files/clear")
    public void clearFiles(@PathVariable("typeid") Long typeid){
        log.info("DocumentController: clearFiles, docid-{}", typeid);
        documentTypeService.clearFiles(typeid);
        log.info("DocumentController: clearFiles - done, docid-{}", typeid);
    }


}