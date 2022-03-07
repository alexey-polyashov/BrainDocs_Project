package com.braindocs.dto.organization;

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
}
