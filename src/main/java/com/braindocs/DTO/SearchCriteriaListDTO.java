package com.braindocs.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchCriteriaListDTO {
    List<SearchCriteriaDTO> filter;
    private Integer page = 0;
    private Integer recordsOnPage = 10;
}
