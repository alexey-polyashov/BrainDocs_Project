package com.braindocs.dto.tasks;

import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.users.UserNameDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    private String createTime;
    @NotNull(message = "Не указан вид задачи")
    private TaskTypeDTO taskType;
    @NotNull(message = "Не указан заголовок задачи")
    private String heading;
    private String content;
    //@Positive(message = "Не верный статус задачи")
    private Long status;//1 активна, 2- выполнена, 3- отменена
    @NotNull(message = "Не указан автор задачи")
    private UserNameDTO author;
    private Set<TaskSubjectDTO> subjects;
    private Boolean marked;

    private List<FileDTO> files;
}
