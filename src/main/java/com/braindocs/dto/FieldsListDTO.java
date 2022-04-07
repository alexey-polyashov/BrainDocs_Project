package com.braindocs.dto;

import com.braindocs.common.SelectableType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldsListDTO {
    private String name;
    private String key;
    private SelectableType selectType;
    private List<String> validOperations;
    private String type;
    private Boolean defaultOn;
}
