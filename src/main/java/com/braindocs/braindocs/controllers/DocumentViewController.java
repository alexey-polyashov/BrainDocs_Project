package com.braindocs.braindocs.controllers;


import com.braindocs.braindocs.DTO.documents.DocumentTypeDTO;
import com.braindocs.braindocs.services.documents.DocumentTypeService;
import com.braindocs.braindocs.services.mappers.DocumentTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doc_view")
public class DocumentViewController {

    private final DocumentTypeService documentTypeService;
    private final DocumentTypeMapper documentTypeMapper;

    @PostMapping
    public String addView(@RequestParam(name = "name_view") String nameView, @RequestParam(name = "add_view") MultipartFile file) {
        return documentTypeService.addView(nameView,file);
    }

    @GetMapping("/get_doc_view/{id}")
    public DocumentTypeDTO getView(@PathVariable Long id){
        return documentTypeMapper.toDTO(documentTypeService.findById(id));
    }

    @DeleteMapping("/delete/{id}")
    public String deleteView(@PathVariable Long id){
        return documentTypeService.deleteById(id);
    }

    @GetMapping("/findAll")
    public List<DocumentTypeDTO> findAllDocumets() {
        return documentTypeService.findAll().stream()
                .map(documentTypeMapper::toDTO)
                .collect(Collectors.toList());
    }
}