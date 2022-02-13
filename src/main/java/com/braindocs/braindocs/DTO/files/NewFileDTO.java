package com.braindocs.braindocs.DTO.files;

import com.braindocs.braindocs.DTO.users.UserNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class NewFileDTO {

    @NotEmpty(message = "Не указано наименование файла")
    private String name;
    private String describe;
    private String originalFilename;
    private String fileType;
    @NotEmpty(message = "Не указан автор файла")
    private UserNameDTO author;

}
