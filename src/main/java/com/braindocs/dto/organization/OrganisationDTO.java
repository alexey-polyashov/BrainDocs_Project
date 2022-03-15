package com.braindocs.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class OrganisationDTO {

    private String name;
    @Size(max = 11, message = "ИНН должен быть не более 11 символов")
    private String inn;
    @Size(max = 9, message = "КПП должен быть не более 9 символов")
    private String kpp;
    private List<OrganisationContactsDTO> contacts;
    private Long id;
    private Boolean marked;

}
