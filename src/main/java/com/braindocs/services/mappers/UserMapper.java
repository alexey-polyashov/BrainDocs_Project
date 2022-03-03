package com.braindocs.services.mappers;

import com.braindocs.dto.organization.OrganisationNameDTO;
import com.braindocs.dto.users.NewUserDTO;
import com.braindocs.dto.users.UserDTO;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final OrganisationService organisationService;
    private final UserContactMapper userContactMapper;
    private final UserRoleMapper userRoleMapper;

    public UserDTO toDTO(UserModel userModel){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userModel.getId());
        userDTO.setLogin(userModel.getLogin());
        userDTO.setEmail(userModel.getEmail());
        userDTO.setFullname(userModel.getFullname());
        userDTO.setOrganisation(new OrganisationNameDTO(userModel.getOrganisation()));
        userDTO.setContacts(userModel.getContacts().stream().map(userContactMapper::toDTO).collect(Collectors.toList()));
        userDTO.setRoles(userModel.getRoles().stream().map(userRoleMapper::toDTO).collect(Collectors.toList()));
        userDTO.setShortname(userModel.getShortname());
        userDTO.setMale(userModel.getMale());
        userDTO.setBirthday(userModel.getBirthday());
        userDTO.setConfirmed(userModel.getConfirmed());
        return userDTO;
    }

    public UserModel toModel(NewUserDTO dto) {
        UserModel user = new UserModel();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFullname(dto.getFullname());
        user.setOrganisation(organisationService.findById(dto.getOrganisationId()));
        user.setShortname(dto.getShortname());
        user.setMale(dto.getMale());
        user.setBirthday(dto.getBirthday());
        user.setConfirmed(false);
        return user;
    }

    public UserModel toModel(UserDTO dto) {
        UserModel user = new UserModel();
        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setFullname(dto.getFullname());
        user.setOrganisation(
                organisationService.findById(
                        dto.getOrganisation().getId()));
        user.setShortname(dto.getShortname());
        user.setMale(dto.getMale());
        user.setBirthday(dto.getBirthday());
        user.setContacts(dto.getContacts().stream().map(userContactMapper::toModel).collect(Collectors.toList()));
        return user;
    }

}
