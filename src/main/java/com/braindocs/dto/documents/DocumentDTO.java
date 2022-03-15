package com.braindocs.dto.documents;

import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.organization.OrganisationNameDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.dto.validators.DateValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
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
    private UserNameDTO author;
    private UserNameDTO responsible;
    @NotNull(message = "Не указана организация")
    private OrganisationNameDTO organisation;
    private Long id;
    private Boolean marked;

    private List<FileDTO> files;

}
