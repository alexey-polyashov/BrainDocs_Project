package com.braindocs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class FieldsListDTO {
    private String name;
    private String key;
    private String source;
    private Set<String> validOperations;
    private String type;
    private Boolean defaultOn;
}
