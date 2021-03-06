package com.braindocs.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.braindocs.dto.users.UserNameDTO;

@Getter
@Setter
@NoArgsConstructor
public class TaskSubjectDTO {

    private String subjectType;
    private String number;
    private String date;
    private String heading;
    private Long id;
    private UserNameDTO author;

}
