package com.braindocs.braindocs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaDTO {
    private String key;
    private String operation;
    private String value;
}
