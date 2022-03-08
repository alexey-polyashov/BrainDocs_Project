package com.braindocs.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskResultDTO {

    private Long id;
    private String typeName;
    private Boolean marked;

}
