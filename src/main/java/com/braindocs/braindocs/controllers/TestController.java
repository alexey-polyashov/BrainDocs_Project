package com.braindocs.braindocs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/test")
    public String testMethod(){
        return "It works";
    }


}
