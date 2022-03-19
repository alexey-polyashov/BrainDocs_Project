package com.braindocs.services.mappers;

import com.braindocs.dto.tasks.TaskCommentDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.models.tasks.TaskCommentModel;
import com.braindocs.services.OptionService;
import com.braindocs.services.tasks.TasksService;
import com.braindocs.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TaskCommentMapper {

    private final UserService userService;
    private final OptionService optionService;
    private final TasksService tasksService;


    @Autowired
    public TaskCommentMapper(UserService userService, OptionService optionService,
                             @Lazy TasksService tasksService) {
        this.userService = userService;
        this.optionService = optionService;
        this.tasksService = tasksService;

    }

    public TaskCommentDTO toDTO(TaskCommentModel model) {
        TaskCommentDTO dto = new TaskCommentDTO();
        dto.setId(model.getId());
        dto.setTaskId(model.getTask().getId());
        dto.setAuthor(new UserNameDTO(model.getAuthor()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        dto.setCreateTime(dtf.format(model.getCreateTime()));
        dto.setComment(model.getComment());
        return dto;
    }

    public TaskCommentModel toModel(TaskCommentDTO dto) {
        TaskCommentModel model = new TaskCommentModel();
        model.setId(model.getId());
        model.setTask(
                tasksService.findById(dto.getTaskId()));
        model.setAuthor(
                userService.findById(dto.getAuthor().getId()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        model.setCreateTime(LocalDateTime.parse(dto.getCreateTime(), dtf));
        model.setComment(dto.getComment());
        return model;
    }

    public TaskCommentModel moveChanges(TaskCommentModel receiver, TaskCommentDTO source) {
        receiver.setAuthor(
                userService.findById(source.getAuthor().getId()));
        receiver.setComment(source.getComment());
        return receiver;
    }

}
