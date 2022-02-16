package com.braindocs.braindocs.DTO.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentTypeDTO {

    private String name;
    private Long id;
    private Boolean marked;

}
