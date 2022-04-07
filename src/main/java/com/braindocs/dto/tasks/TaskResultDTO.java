package com.braindocs.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResultDTO {

    private Long id;
    private String resultName;
    private Integer resultType;//1-positive, 2-withComments, 3-negative, 4-other
    private Boolean marked;

}
