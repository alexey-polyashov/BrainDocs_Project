package com.braindocs.braindocs.DTO.documents;

import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.organization.OrganisationNameDTO;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import com.braindocs.braindocs.DTO.validators.DateValidator;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentDTO {

    private DocumentTypeNameDTO documentType;
    @NotEmpty(message = "Не указан номер документа")
    private String number;
    @DateValidator(message = "Не верно указана дата документа")
    private String documentDate;
    @NotBlank(message = "Не указан заголовок документа")
    private String heading;
    private String content;
    private UserNameDTO author;
    private UserNameDTO responsible;
    private OrganisationNameDTO organisation;
    private Long id;
    private Boolean marked;

    private List<FileDTO> files;

}
