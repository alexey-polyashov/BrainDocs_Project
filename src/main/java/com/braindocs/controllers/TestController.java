package com.braindocs.controllers;

import com.braindocs.dto.documents.DocumentTypeNameDTO;
import com.braindocs.dto.organization.OrganisationNameDTO;
import com.braindocs.dto.users.UserNameDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping("/users")
    public UserNameDTO[] getUsersList(){
        UserNameDTO[] users = new UserNameDTO[15];
        users[0] = UserNameDTO.builder().shortname("Сильвер С.").id(1L).build();
        users[1] = UserNameDTO.builder().shortname("Ханс Ц.").id(2L).build();
        users[2] = UserNameDTO.builder().shortname("Шварцнегер А.").id(3L).build();
        for (int i = 3; i < users.length; i++) {
            users[i] = new UserNameDTO();
            users[i].setId(Long.valueOf(i+1L));
            users[i].setShortname("User " + i);
        }
        return users;
    }

}
