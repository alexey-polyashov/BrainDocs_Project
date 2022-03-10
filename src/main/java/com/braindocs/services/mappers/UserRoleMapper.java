package com.braindocs.services.mappers;

import com.braindocs.dto.users.RoleDTO;
import com.braindocs.models.users.UserRoleModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleMapper {

    private final ModelMapper modelMapper;

    public RoleDTO toDTO(UserRoleModel userRoleModel) {
        return modelMapper.map(userRoleModel, RoleDTO.class);
    }
}
