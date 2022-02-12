package com.braindocs.braindocs.DTO.files;

import com.braindocs.braindocs.DTO.users.UserNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class NewFileDTOwithData {

    @NotEmpty(message = "Не указано наименование файла")
    private String name;
    private String describtion;
    private String originalFilename;
    @NotEmpty(message = "Не указан тип файла")
    private String fileType;
    @NotEmpty(message = "Не указан автор файла")
    private UserNameDTO author;
    @NotEmpty(message = "Не выбран файл")
    private MultipartFile fileData;

}
