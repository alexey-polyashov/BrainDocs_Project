package com.braindocs.services.mappers;

import com.braindocs.dto.organization.OrganisationDTO;
import com.braindocs.models.organisations.OrganisationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationMapper {

    private final OrganisationContactMapper organisationContactMapper;

    public OrganisationDTO toDTO(OrganisationModel organisationModel){
        OrganisationDTO dto = new OrganisationDTO();
        dto.setId(organisationModel.getId());
        dto.setInn(organisationModel.getInn());
        dto.setKpp(organisationModel.getKpp());
        dto.setName(organisationModel.getName());
        dto.setMarked(organisationModel.getMarked());
        dto.setContacts(organisationModel.getContacts().stream().map(organisationContactMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public OrganisationModel toModel(OrganisationDTO dto){
        OrganisationModel organisationModel = new OrganisationModel();
        organisationModel.setId(dto.getId());
        organisationModel.setInn(dto.getInn());
        organisationModel.setKpp(dto.getKpp());
        organisationModel.setName(dto.getName());
        organisationModel.setMarked(dto.getMarked());
        organisationModel.setContacts(dto.getContacts().stream().map(organisationContactMapper::toModel).collect(Collectors.toList()));
        return organisationModel;
    }

}
