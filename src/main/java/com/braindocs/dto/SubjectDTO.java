package com.braindocs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubjectDTO {
    private String heading = "";
    private String subjectType = "Документ";
    private Long id = 0L;
}
