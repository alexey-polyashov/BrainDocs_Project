package com.braindocs.services.mappers;

import com.braindocs.dto.organization.OrganisationContactsDTO;
import com.braindocs.models.organisations.OrganisationContactsModel;
import com.braindocs.services.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationContactMapper {

    private final ContactTypeService contactTypeService;

    public OrganisationContactsDTO toDTO(OrganisationContactsModel organisationContactsModel) {
        OrganisationContactsDTO dto = new OrganisationContactsDTO();
        dto.setId(organisationContactsModel.getId());
        dto.setTypeId(organisationContactsModel.getType().getId());
        dto.setTypeName(organisationContactsModel.getType().getName());
        dto.setPresent(organisationContactsModel.getPresent());
        dto.setOrganisationId(organisationContactsModel.getOrganisation());
        return dto;
    }

    public OrganisationContactsModel toModel(OrganisationContactsDTO organisationContactsDTO) {
        OrganisationContactsModel model = new OrganisationContactsModel();
        model.setId(organisationContactsDTO.getId());
        model.setType(contactTypeService.findById(organisationContactsDTO.getTypeId()));
        model.setPresent(organisationContactsDTO.getPresent());
        model.setOrganisation(organisationContactsDTO.getOrganisationId());
        return model;
    }

}
