package com.braindocs.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutorResultDTO {

    private Long resultId;
    private String resultComment;
    private Long executor;
    private String executeDateTime;

}
