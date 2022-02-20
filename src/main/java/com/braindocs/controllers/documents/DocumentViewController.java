package com.braindocs.controllers.documents;


import com.braindocs.dto.documents.DocumentTypeDTO;
import com.braindocs.dto.documents.NewDocumentTypeDTO;
import com.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.services.documents.DocumentTypeService;
import com.braindocs.services.mappers.DocumentTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/document-types")
public class DocumentViewController {

    private final DocumentTypeService documentTypeService;
    private final DocumentTypeMapper documentTypeMapper;

    @PostMapping
    public Long addView(@RequestBody NewDocumentTypeDTO documentTypeDTO) {
        DocumentTypeModel docType = documentTypeMapper.toModel(documentTypeDTO);
        return documentTypeService.addType(docType);
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