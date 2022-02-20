package com.braindocs.dto.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentTypeDTO {

    private String name;
    private Long id;
    private Boolean marked;

}
