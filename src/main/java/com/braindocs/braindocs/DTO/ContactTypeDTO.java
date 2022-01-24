package com.braindocs.braindocs.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactTypeDTO {

    private Long id;
    private String name;
    private Boolean marked;

}
