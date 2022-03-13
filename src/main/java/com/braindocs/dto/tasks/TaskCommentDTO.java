package com.braindocs.dto.tasks;

import com.braindocs.dto.users.UserNameDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class TaskCommentDTO {
    private Long id;
    private Long taskId;
    private UserNameDTO author;
    private String comment;
    private String createTime;
}
