package com.braindocs.braindocs.DTO.documents;

import com.braindocs.braindocs.DTO.files.FileDTO;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentDTO {

    @NotEmpty(message = "Не указан вид документа")
    private Long documentTypeId;
    private Long documentTypeName;
    @NotEmpty(message = "Не указан номер документа")
    private String number;
    @NotEmpty(message = "Не указана дата документа")
    private Date documentDate;
    @NotEmpty(message = "Не указан заголовок документа")
    private String heading;
    private String content;
    @NotEmpty(message = "Не указан автор документа")
    private Long authorId;
    private String authorName;
    private Long responsibleId;
    private String responsibleName;
    @NotEmpty(message = "Не указана организация")
    private Long organisationId;
    private String organisationName;
    private Long id;
    private Boolean marked;

    private List<FileDTO> files;

}
