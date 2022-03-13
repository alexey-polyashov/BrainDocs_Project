package com.braindocs.dto.tasks;

import com.braindocs.dto.users.UserNameDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskExecutorDTO {

    private Long id;
    private Long taskId;
    private UserNameDTO executor;
    private String createdAt;
    private String planedDate;
    private String dateOfCompletion;
    private String comment;
    private TaskResultDTO result;
    private Long status; //1 ожидает выполнения, 2- в работе, 3- выполнена, 4- отменена

}
