package com.braindocs.controllers.documents;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.dto.FieldsListDTO;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.SearchCriteriaListDTO;
import com.braindocs.dto.organization.OrganisationDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.models.organisations.OrganisationModel;
import com.braindocs.repositories.specifications.OrganisationSpecificationBuilder;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.mappers.OrganisationMapper;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organisations")
@Slf4j
public class OrganisqtionController {

    private final OrganisationService organisationService;
    private final UserService userService;
    private final OrganisationMapper organisationMapper;

    private static final String STRING_TYPE = "String";
    private static final String LONG_TYPE = "Long";

    @GetMapping(value="/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Наименование организации", "name", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("ИНН организации", "inn", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("КПП организации", "kpp", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Руководитель", "manager", "/api/v1/users", Arrays.asList(":"), LONG_TYPE, false));
        log.info("DocumentController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @PostMapping
    public Long addView(@RequestBody OrganisationDTO organisationDTO) {
        OrganisationModel organisation = organisationMapper.toModel(organisationDTO);
        return organisationService.add(organisation);
    }

    @PostMapping("/{orgid}")
    public Long change(@PathVariable Long orgid, @RequestBody OrganisationDTO organisationDTO) {
        if(orgid==0){
            throw new BadRequestException("id должен быть отличен от 0");
        }
        return organisationService.change(orgid, organisationMapper.toModel(organisationDTO));
    }

    @GetMapping("/{id}")
    public OrganisationDTO findById(@PathVariable Long id){
        return organisationMapper.toDTO(organisationService.findById(id));
    }

    @PostMapping("/search")
    public Page<OrganisationDTO> search(@RequestBody SearchCriteriaListDTO requestDTO) {
        log.info("DocumentViewController: search");
        List<SearchCriteriaDTO> filter = requestDTO.getFilter();
        Integer page = requestDTO.getPage();
        Integer recordsOnPage = requestDTO.getRecordsOnPage();
        Page<OrganisationModel> organisationPage =
                organisationService.getOrganisationByFields(
                    page,
                    recordsOnPage,
                        filter);
        Page<OrganisationDTO> organisationDTOPage = organisationPage.map(organisationMapper::toDTO);
        log.info("DocumentViewController: search return {} elements", organisationDTOPage.getSize());
        return organisationDTOPage;
    }

    @DeleteMapping("/finally/{orgid}")
    public void delete(@PathVariable Long orgid){
        organisationService.deleteById(orgid);
    }

    @DeleteMapping("/{orgid}")
    public void mark(@PathVariable Long orgid){
        organisationService.setMark(orgid, true);
    }

    @PostMapping("/unmark/{orgid}")
    public void unMark(@PathVariable Long orgid){
        organisationService.setMark(orgid, false);
    }

    @GetMapping("")
    public List<OrganisationDTO> findAll(@RequestParam(name = "marked", defaultValue = "off", required = false) String marked) {
        if(!EnumUtils.isValidEnum(MarkedRequestValue.class, marked.toUpperCase(Locale.ROOT))){
            throw new BadRequestException("Недопустимое значение параметра marked");
        }
        return organisationService.findAll(MarkedRequestValue.valueOf(marked.toUpperCase(Locale.ROOT)))
                .stream()
                .map(organisationMapper::toDTO)
                .collect(Collectors.toList());
    }

}
