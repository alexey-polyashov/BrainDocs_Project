package com.braindocs.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSubjectDTO {

    private Long id;
    private String typeName;
    private Boolean marked;

}
