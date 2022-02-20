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

    @GetMapping("/organisations")
    public OrganisationNameDTO[] getOrganisationsList(){
        OrganisationNameDTO[] orgs = new OrganisationNameDTO[10];
        orgs[0] = OrganisationNameDTO.builder().name("'ООО \"Ромашка\"'").id(1L).build();
        orgs[1] = OrganisationNameDTO.builder().name("'ИП Иванов'").id(2L).build();
        for (int i = 2; i < orgs.length; i++) {
            orgs[i] = new OrganisationNameDTO();
            orgs[i].setId(new Long(i+1));
            orgs[i].setName("Organisation " + i);
        }
        return orgs;
    }

    @GetMapping("/users")
    public UserNameDTO[] getUsersList(){
        UserNameDTO[] users = new UserNameDTO[15];
        users[0] = UserNameDTO.builder().shortname("Сильвер С.").id(1L).build();
        users[1] = UserNameDTO.builder().shortname("Ханс Ц.").id(2L).build();
        users[2] = UserNameDTO.builder().shortname("Шварцнегер А.").id(3L).build();
        for (int i = 3; i < users.length; i++) {
            users[i] = new UserNameDTO();
            users[i].setId((new Long(i+1));
            users[i].setShortname("User " + i);
        }
        return users;
    }

    @GetMapping("/doc_types")
    public DocumentTypeNameDTO[] getDocumentTypesList(){
        DocumentTypeNameDTO[] docTypes = new DocumentTypeNameDTO[10];
        docTypes[0] = DocumentTypeNameDTO.builder().name("Приказы").id(1L).build();
        docTypes[1] = DocumentTypeNameDTO.builder().name("Договора").id(2L).build();
        docTypes[2] = DocumentTypeNameDTO.builder().name("Прочие").id(3L).build();
        for (int i = 3; i < docTypes.length; i++) {
            docTypes[i] = new DocumentTypeNameDTO();
            docTypes[i].setId(new Long(i+1));
            docTypes[i].setName("Doc type " + i);
        }
        return docTypes;
    }

}
