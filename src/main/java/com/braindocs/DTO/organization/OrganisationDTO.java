package com.braindocs.DTO.organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class OrganisationDTO {

    private String name;
    private String inn;
    private String kpp;
    private List<OrganisationContactsDTO> contacts;
    private Long id;
    private Boolean marked;

}
