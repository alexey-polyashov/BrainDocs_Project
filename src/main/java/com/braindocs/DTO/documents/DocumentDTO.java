package com.braindocs.DTO.documents;

import com.braindocs.DTO.files.FileDTO;
import com.braindocs.DTO.organization.OrganisationNameDTO;
import com.braindocs.DTO.users.UserNameDTO;
import com.braindocs.DTO.validators.DateValidator;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentDTO {

    @NotNull(message = "Не указан вид документа")
    private DocumentTypeNameDTO documentType;
    @NotEmpty(message = "Не указан номер документа")
    private String number;
    @DateValidator(message = "Не верно указана дата документа")
    private String documentDate;
    @NotBlank(message = "Не указан заголовок документа")
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
