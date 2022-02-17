package com.braindocs.DTO.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganisationContactsDTO {

    private Long organisationId;
    private String organisationName;
    private Long typeId;
    private String typeName;
    private String present;
    private Long id;
    private Boolean marked;
}
