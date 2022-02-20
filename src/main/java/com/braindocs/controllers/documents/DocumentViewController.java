package com.braindocs.controllers.documents;


import com.braindocs.dto.FieldsListDTO;
import com.braindocs.dto.documents.DocumentTypeDTO;
import com.braindocs.dto.documents.NewDocumentTypeDTO;
import com.braindocs.exceptions.AnyOtherException;
import com.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.services.documents.DocumentTypeService;
import com.braindocs.services.mappers.DocumentTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents/types")
@Slf4j
public class DocumentViewController {

    private final DocumentTypeService documentTypeService;
    private final DocumentTypeMapper documentTypeMapper;

    private static final String STRING_TYPE = "String";
    private static final String LONG_TYPE = "Long";
    private static final String DATE_TYPE = "Date";

    @GetMapping(value="/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Наименование вида документа", "name", "", Arrays.asList(":"), STRING_TYPE, false));
        log.info("DocumentController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @PostMapping
    public Long addView(@RequestBody NewDocumentTypeDTO documentTypeDTO) {
        DocumentTypeModel docType = documentTypeMapper.toModel(documentTypeDTO);
        return documentTypeService.addType(docType);
    }

    @PostMapping("/{typeid}")
    public DocumentTypeDTO changeView(@PathVariable Long typeid, @RequestBody NewDocumentTypeDTO documentTypeDTO) {
        if(typeid==0){
            throw new AnyOtherException("id должен быть отличен от 0");
        }
        DocumentTypeModel docType = documentTypeMapper.toModel(documentTypeDTO);
        return documentTypeMapper.toDTO(documentTypeService.changeType(typeid, docType));
    }

    @GetMapping("/{id}")
    public DocumentTypeDTO findById(@PathVariable Long id){
        return documentTypeMapper.toDTO(documentTypeService.findById(id));
    }

    @DeleteMapping("/finally/{id}")
    public void deleteView(@PathVariable Long id){
        documentTypeService.deleteById(id);
    }

    @DeleteMapping("/{id}")
    public void markView(@PathVariable Long id){
        documentTypeService.markById(id);
    }

    @GetMapping("")
    public List<DocumentTypeDTO> findAll() {
        return documentTypeService.findAll().stream()
                .map(documentTypeMapper::toDTO)
                .collect(Collectors.toList());
    }
}