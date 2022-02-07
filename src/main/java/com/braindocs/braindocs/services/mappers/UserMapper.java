package com.braindocs.braindocs.services.mappers;

import com.braindocs.braindocs.DTO.users.UserDTO;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final UserService userService;
    private final OrganisationMapper organisationMapper;

    public UserDTO toDTO(UserModel userModel){
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(userModel.getLogin());
        userDTO.setEmail(userModel.getEmail());
        userDTO.setFullname(userModel.getFullname());
        userDTO.setOrganisation(organisationMapper.toDTO(userModel.getOrganisation()));
        userDTO.setLogin(userModel.getLogin());
        return userDTO;
    }
}
