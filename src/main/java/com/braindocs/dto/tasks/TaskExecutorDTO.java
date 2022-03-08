package com.braindocs.dto.tasks;

import com.braindocs.dto.users.UserNameDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class TaskExecutorDTO {

    private Long id;
    private UserNameDTO executor;
    private Date planedDate;
    private Date dateOfComletion;
    private String comment;
    private TaskResultDTO result;
    private Long status; //1 ожидает выполнения, 2- в работе, 3- выполнена, 4- отменена

}
