package com.braindocs.braindocs.controllers;


import com.braindocs.braindocs.DTO.documents.DocumentViewDTO;
import com.braindocs.braindocs.services.documents.DocumentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doc_view")
public class DocumentViewController {

    private final DocumentViewService documentViewService;

    @PostMapping
    public String addView(@RequestParam(name = "name_view") String nameView, @RequestParam(name = "add_view") MultipartFile file) {
        return documentViewService.addView(nameView,file);
    }

    @GetMapping("/get_doc_view/{id}")
    public String getView(@PathVariable Long id){
        return documentViewService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteView(@PathVariable Long id){
        return documentViewService.deleteById(id);
    }

    @GetMapping("/findAll")
    public List<DocumentViewDTO> findAllDocumets() {
        return documentViewService.findAll().stream()
                .map(docStorMod -> new DocumentViewDTO(docStorMod.getId(),
                        docStorMod.getName(),
                        docStorMod.getDocumentData()))
                .collect(Collectors.toList());
    }
}