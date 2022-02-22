package com.braindocs.services.mappers;

import com.braindocs.dto.users.NewUserDTO;
import com.braindocs.dto.users.UserDTO;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final OrganisationService organisationService;
    private final ModelMapper modelMapper;
    private final OrganisationMapper organisationMapper;

    public UserDTO toDTO(UserModel userModel){
        return modelMapper.map(userModel, UserDTO.class);
//        UserDTO userDTO = new UserDTO();
//        userDTO.setLogin(userModel.getLogin());
//        userDTO.setEmail(userModel.getEmail());
//        userDTO.setFullname(userModel.getFullname());
//        userDTO.setOrganisation(organisationMapper.toDTO(userModel.getOrganisation()));
//        userDTO.setLogin(userModel.getLogin());
//        return userDTO;
    }

    public UserModel toModel(NewUserDTO dto) {
        UserModel user = new UserModel();
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setFullname(dto.getFullname());
        user.setOrganisation(organisationService.findById(dto.getOrganisationId()));
        user.setLogin(dto.getLogin());
        return user;
    }

}
