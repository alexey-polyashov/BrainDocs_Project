package com.braindocs.braindocs.DTO.files;

import com.braindocs.braindocs.models.files.FilesModel;
import com.braindocs.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class FileDTOwithData {

    private Long id;
    @NotEmpty(message = "Не указано наименование файла")
    private String name;
    private Integer storageType;
    private String describtion;
    @NotEmpty(message = "Не указан размер файла")
    private Long filesize;
    @NotEmpty(message = "Не указан тип файла")
    private String filetype;
    @NotEmpty(message = "Не указан автор файла")
    private Long authorId;
    private String authorName;
    private String content;
    private String sourcePath;
    @NotEmpty(message = "Не выбран файл")
    private MultipartFile fileData;

}
