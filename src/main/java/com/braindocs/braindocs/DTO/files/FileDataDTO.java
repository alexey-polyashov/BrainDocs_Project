package com.braindocs.braindocs.DTO.files;

import com.braindocs.braindocs.DTO.users.UserNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class FileDataDTO {

    private Long id;
    private String name;
    private String contentType;
    private String fileType;
    private byte[] fileData;

}
