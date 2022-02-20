package com.braindocs.dto.organization;

import com.braindocs.models.organisations.OrganisationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrganisationNameDTO {
    @NotEmpty(message = "Не указан идентификатор организации")
    private Long id;
    private String name;

    public OrganisationNameDTO(OrganisationModel orgModel) {
        this.id = orgModel.getId();
        this.name = orgModel.getName();
    }
}
