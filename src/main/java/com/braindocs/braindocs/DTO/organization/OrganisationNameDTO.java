package com.braindocs.braindocs.DTO.organization;

import com.braindocs.braindocs.models.organisations.OrganisationModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class OrganisationNameDTO {
    @NotEmpty(message = "Не указан идентификатор организации")
    private Long id;
    private String name;

    public OrganisationNameDTO(OrganisationModel orgModel) {
        this.id = orgModel.getId();
        this.name = orgModel.getName();
    }
}
