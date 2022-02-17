package com.braindocs.DTO.files;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDataDTO {

    private Long id;
    private String name;
    private String contentType;
    private String fileType;
    private byte[] fileData;

}
