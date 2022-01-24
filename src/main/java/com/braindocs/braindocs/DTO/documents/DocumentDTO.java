package com.braindocs.braindocs.DTO.documents;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
public class DocumentDTO {

    private Long documentTypeId;
    private Long documentTypeName;
    private String number;
    private Date documentDate;
    private String heading;
    private String content;
    private Long authorId;
    private String authorName;
    private Long responsibleId;
    private String responsibleName;
    private Long organisationId;
    private String organisationName;
    private Long id;
    private Boolean marked;

}
