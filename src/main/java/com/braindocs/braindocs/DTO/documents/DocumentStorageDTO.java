package com.braindocs.braindocs.DTO.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentStorageDTO {
    private Long id;
    private String documentData;

    public DocumentStorageDTO(Long id, String documentData) {
        this.id = id;
        this.documentData = documentData;
    }
}
