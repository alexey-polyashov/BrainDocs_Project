package com.braindocs.braindocs.controllers;

import com.braindocs.braindocs.DTO.documents.DocumentTypeNameDTO;
import com.braindocs.braindocs.DTO.organization.OrganisationNameDTO;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class TestController {

    @GetMapping("test")
    public String testMethod(){
        log.info("Test controller: it works");
        return "It works";
    }

    @GetMapping("/organisations")
    public OrganisationNameDTO[] getOrganisationsList(){
        OrganisationNameDTO[] orgs = new OrganisationNameDTO[10];
        for (int i = 0; i < orgs.length; i++) {
            orgs[i] = new OrganisationNameDTO();
            orgs[i].setId((long) i);
            orgs[i].setName("Organisation " + i);
        }
        return orgs;
    }

    @GetMapping("/users")
    public UserNameDTO[] getUsersList(){
        UserNameDTO[] users = new UserNameDTO[15];
        for (int i = 0; i < users.length; i++) {
            users[i] = new UserNameDTO();
            users[i].setId((long) i);
            users[i].setShortname("User " + i);
        }
        return users;
    }

    @GetMapping("/doc_types")
    public DocumentTypeNameDTO[] getDocumentTypesList(){
        DocumentTypeNameDTO[] docTypes = new DocumentTypeNameDTO[10];
        for (int i = 0; i < docTypes.length; i++) {
            docTypes[i] = new DocumentTypeNameDTO();
            docTypes[i].setId((long) i);
            docTypes[i].setName("Doc type " + i);
        }
        return docTypes;
    }

}
