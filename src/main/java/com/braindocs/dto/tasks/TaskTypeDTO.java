package com.braindocs.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskTypeDTO {

    private Long id;
    private String name;
    private Boolean marked;

}
