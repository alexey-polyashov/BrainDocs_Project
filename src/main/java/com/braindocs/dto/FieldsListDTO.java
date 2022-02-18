package com.braindocs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldsListDTO {
    private String name;
    private String key;
    private String endPoint;
    private List<String> validOperations;
    private String type;
    private Boolean defaultOn;
}
