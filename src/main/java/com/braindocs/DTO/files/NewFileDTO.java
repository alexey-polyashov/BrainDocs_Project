package com.braindocs.DTO.files;

import com.braindocs.DTO.users.UserNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class NewFileDTO {

    @NotEmpty(message = "Не указано наименование файла")
    private String name;
    private String description;
    @NotEmpty(message = "Не указан автор файла")
    private UserNameDTO author;
    private String originalFilename;
    private String fileType;


}
