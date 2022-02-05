package com.braindocs.braindocs.DTO.files;

import com.braindocs.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDTOwithData {

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
    private byte[] fileData;

}
