package com.braindocs.services.mappers;

import com.braindocs.dto.users.RoleDTO;
import com.braindocs.models.users.UserRoleModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RoleMapper {

    public RoleDTO toDTO(UserRoleModel roleModel){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleDTO.getId());
        roleDTO.setName(roleDTO.getName());
        roleDTO.setMarked(roleDTO.getMarked());
        return roleDTO;
    }

    public UserRoleModel toModel(RoleDTO dto) {
        UserRoleModel role = new UserRoleModel();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setMarked(dto.getMarked());
        return role;
    }

    public Collection<UserRoleModel> toModel(Collection<RoleDTO> dtos) {
        Collection<UserRoleModel> roles = new ArrayList<>();
        for (RoleDTO dto : dtos) {
            roles.add(toModel(dto));
        }
        return roles;
    }
}
