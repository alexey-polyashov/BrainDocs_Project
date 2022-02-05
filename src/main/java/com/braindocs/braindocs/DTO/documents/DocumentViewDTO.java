package com.braindocs.braindocs.DTO.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentViewDTO {
    private Long id;
    private String name;
    private String documentData;

    public DocumentViewDTO(Long id,String name, String documentData) {
        this.id = id;
        this.name = name;
        this.documentData = documentData;
    }
}
