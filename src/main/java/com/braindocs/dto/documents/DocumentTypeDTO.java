package com.braindocs.dto.documents;

import com.braindocs.dto.files.FileDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DocumentTypeDTO {

    private String name;
    private Long id;
    private Boolean marked;

    private List<FileDTO> files;

}
