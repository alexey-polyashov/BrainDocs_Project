package com.braindocs.braindocs.DTO.documents;

import com.braindocs.braindocs.models.documents.DocumentTypeModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class DocumentTypeNameDTO {
    @NotEmpty(message = "Не указан идентификатор вида документа")
    private Long id;
    private String name;

    public DocumentTypeNameDTO(DocumentTypeModel docType) {
        this.id = docType.getId();
        this.name = docType.getName();
    }
}
