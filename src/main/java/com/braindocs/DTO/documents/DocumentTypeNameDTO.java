package com.braindocs.DTO.documents;

import com.braindocs.models.documents.DocumentTypeModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class DocumentTypeNameDTO {
    @NotEmpty(message = "Не указан идентификатор вида документа")
    private Long id;
    private String name;

    public DocumentTypeNameDTO(DocumentTypeModel docType) {
        this.id = docType.getId();
        this.name = docType.getName();
    }
}
