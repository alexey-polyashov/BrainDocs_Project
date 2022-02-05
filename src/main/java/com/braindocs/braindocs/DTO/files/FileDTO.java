package com.braindocs.braindocs.DTO.files;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDTO {

    private Long id;
    private String name;
    private Integer storageType;
    private String describtion;
    private Long filesize;
    private String filetype;
    private Long authorId;
    private String authorName;
    private String content;
    private String sourcePath;

}
