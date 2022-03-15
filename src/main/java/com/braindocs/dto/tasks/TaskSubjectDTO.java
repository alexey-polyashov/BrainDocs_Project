package com.braindocs.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSubjectDTO {

    private String subjectType;
    private String number;
    private String date;
    private String name;
    private Long id;

}
