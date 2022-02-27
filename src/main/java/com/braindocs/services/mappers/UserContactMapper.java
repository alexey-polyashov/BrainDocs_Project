package com.braindocs.services.mappers;

import com.braindocs.dto.organization.OrganisationContactsDTO;
import com.braindocs.dto.users.UserContactDTO;
import com.braindocs.models.organisations.OrganisationContactsModel;
import com.braindocs.models.users.UserContactModel;
import com.braindocs.services.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserContactMapper {

    private final ContactTypeService contactTypeService;

    public UserContactDTO toDTO(UserContactModel userContactsModel){
        UserContactDTO dto = new UserContactDTO();
        dto.setId(userContactsModel.getId());
        dto.setTypeId(userContactsModel.getType().getId());
        dto.setTypeName(userContactsModel.getType().getName());
        dto.setPresent(userContactsModel.getPresent());
        dto.setUserId(userContactsModel.getUserId());
        dto.setMarked(userContactsModel.getMarked());
        return dto;
    }

    public UserContactModel toModel(UserContactDTO userContactsDTO){
        UserContactModel model = new UserContactModel();
        model.setId(userContactsDTO.getId());
        model.setType(contactTypeService.findById(userContactsDTO.getTypeId()));
        model.setPresent(userContactsDTO.getPresent());
        model.setUserId(userContactsDTO.getUserId());
        model.setMarked(userContactsDTO.getMarked());
        return model;
    }

}
