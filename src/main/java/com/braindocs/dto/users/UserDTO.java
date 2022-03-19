package com.braindocs.dto.users;

import com.braindocs.dto.organization.OrganisationNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {
    private String login;
    private String email;
    private String fullname;
    private OrganisationNameDTO organisation;
    private String shortname;
    private String male; // list of: 'female', 'male'
    private String birthday;
    private List<UserContactDTO> contacts;
    private List<RoleDTO> roles;
    private Long id;
    private Boolean marked;
    private Boolean confirmed;
}
