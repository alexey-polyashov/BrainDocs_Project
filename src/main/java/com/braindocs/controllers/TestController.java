package com.braindocs.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class TestController {

    @GetMapping("/test")
    public List<String> testMethod(){
        log.info("Test controller: it works");
        return Arrays.asList("It works");
    }

}
