package com.braindocs.controllers;

import com.braindocs.dto.ContactTypeDTO;
import com.braindocs.services.ContactTypeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/contacts")
@Slf4j
@Api(value="Contacts controller", tags="Контроллер контактов")
public class ContactController {

    private ModelMapper modelMapper;
    private final ContactTypeService contactTypeService;

    @GetMapping("/types")
    public List<ContactTypeDTO> getTypes(){
        return contactTypeService.findAll()
                .stream()
                .map(p->modelMapper.map(p,ContactTypeDTO.class))
                .collect(Collectors.toList());
    }
}
