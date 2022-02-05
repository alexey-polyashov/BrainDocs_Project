package com.braindocs.braindocs.controllers;

import com.braindocs.braindocs.DTO.documents.DocumentStorageDTO;
import com.braindocs.braindocs.models.documents.DocumentStorageModel;
import com.braindocs.braindocs.services.documents.DocumentStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final DocumentStorageService documentStorageService;


    @GetMapping("/test")
    public String testMethod(){
        return "It works";
    }

    @GetMapping("/findAll")
    public List<DocumentStorageDTO> findAllDocumets() {
        return documentStorageService.findAll().stream()
                .map(docStorMod -> new DocumentStorageDTO(docStorMod.getId(),
                        docStorMod.getDocumentData()))
                .collect(Collectors.toList());
    }

}
