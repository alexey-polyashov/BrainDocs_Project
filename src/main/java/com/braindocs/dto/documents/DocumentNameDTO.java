package com.braindocs.dto.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentNameDTO {

    private DocumentTypeNameDTO documentType;
    private String number;
    private String documentDate;
    private String heading;
    private Long id;

}
