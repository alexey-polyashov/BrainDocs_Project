package com.braindocs.braindocs.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("test")
    public String testMethod(){
        log.info("Test controller: it works");
        return "It works";
    }

}
