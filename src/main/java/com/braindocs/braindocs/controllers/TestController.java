package com.braindocs.braindocs.controllers;

import com.braindocs.braindocs.DTO.documents.DocumentViewDTO;
import com.braindocs.braindocs.services.documents.DocumentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/test")
    public String testMethod(){
        return "It works";
    }


}
