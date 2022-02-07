package com.braindocs.braindocs.DTO.documents;

import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.organization.OrganisationNameDTO;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentDTO {

    @NotEmpty(message = "Не указан вид документа")
    private DocumentTypeNameDTO documentType;
    @NotEmpty(message = "Не указан номер документа")
    private String number;
    @NotEmpty(message = "Не указана дата документа")
    private Date documentDate;
    @NotEmpty(message = "Не указан заголовок документа")
    private String heading;
    private String content;
    @NotNull(message = "Не указан автор документа")
    private UserNameDTO author;
    private UserNameDTO responsible;
    @NotNull(message = "Не указана организация")
    private OrganisationNameDTO organisation;
    private Long id;
    private Boolean marked;

    private List<FileDTO> files;

}
