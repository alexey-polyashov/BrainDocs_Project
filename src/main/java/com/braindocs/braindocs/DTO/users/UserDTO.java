package com.braindocs.braindocs.DTO.users;

import com.braindocs.braindocs.DTO.organization.OrganisationDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {
    private String login;
    private String email;
    private String fullname;
    private OrganisationDTO organisation;
    private String shortname;
    private String male; // list of: 'female', 'male'
    private List<UserContactDTO> contacts;
    private List<RoleDTO> roles;
    private Long id;
    private Boolean marked;
}
