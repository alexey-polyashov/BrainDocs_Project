package com.braindocs.dto.files;

import com.braindocs.dto.users.UserNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class FileDTO {

    private Long id;
    @NotEmpty(message = "Не указано наименование файла")
    private String name;
    private Integer storageType;
    private String description;
    @NotEmpty(message = "Не указан размер файла")
    private Long fileSize;
    @NotEmpty(message = "Не указан автор файла")
    private UserNameDTO author;
    private String contentType;
    @NotEmpty(message = "Не указан тип файла")
    private String fileType;
    private String link;

}
