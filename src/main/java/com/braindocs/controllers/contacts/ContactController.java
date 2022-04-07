package com.braindocs.controllers.contacts;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Utils;
import com.braindocs.dto.ContactTypeDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.models.ContactTypeModel;
import com.braindocs.services.ContactTypeService;
import com.braindocs.services.mappers.ContactTypeMapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/contacts")
@Slf4j
@Api(value = "Contacts controller", tags = "Контроллер контактов")
public class ContactController {

    private final ContactTypeMapper contactTypeMapper;
    private final ContactTypeService contactTypeService;

    @GetMapping("/types")
    public List<ContactTypeDTO> getTypes(
            @RequestParam(name = "marked", defaultValue = "off", required = false) String marked) {
        log.info("ContactController: getTypes, marked -{} ", marked);
        if (!Utils.isValidEnum(MarkedRequestValue.class, marked.toUpperCase(Locale.ROOT))) {
            throw new BadRequestException("Недопустимое значение параметра marked");
        }
        List<ContactTypeModel> typeModels = contactTypeService.findAll(MarkedRequestValue.valueOf(marked.toUpperCase(Locale.ROOT)));
        log.info("ContactController: getDocuments return {} elements", typeModels.size());
        return typeModels
                .stream()
                .map(contactTypeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
