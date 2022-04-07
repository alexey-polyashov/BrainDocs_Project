package com.braindocs.services.mappers;

import com.braindocs.common.Options;
import com.braindocs.dto.tasks.TaskExecutorDTO;
import com.braindocs.dto.tasks.TaskExecutorDtoExt;
import com.braindocs.dto.tasks.TaskResultDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.models.tasks.TaskResultsModel;
import com.braindocs.repositories.tasks.TaskResultsRepository;
import com.braindocs.services.tasks.TasksService;
import com.braindocs.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskExecutorMapper {

    private final UserService userService;
    private final Options options;
    private final TasksService tasksService;
    private final TaskResultsRepository taskResultsRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskExecutorMapper(UserService userService,
                              Options options,
                              TaskResultsRepository taskResultsRepository,
                              TaskMapper taskMapper,
                              @Lazy TasksService tasksService) {
        this.userService = userService;
        this.options = options;
        this.tasksService = tasksService;
        this.taskResultsRepository = taskResultsRepository;
        this.taskMapper = taskMapper;
    }

    public TaskExecutorDTO toDTO(TaskExecutorModel model) {
        TaskExecutorDTO dto = new TaskExecutorDTO();
        dto.setId(model.getId());
        dto.setExecutor(new UserNameDTO(model.getExecutor()));
        dto.setTaskId(model.getTask().getId());
        dto.setCreatedAt(options.convertDateTimeToString(model.getCreateTime()));
        dto.setPlanedDate(options.convertDateTimeToString(model.getPlanedDate()));
        dto.setDateOfCompletion(options.convertDateTimeToString(model.getDateOfComletion()));
        if(model.getComment()!=null) {
            dto.setComment(model.getComment());
        }
        TaskResultsModel taskResult = model.getResult();
        if(taskResult!=null) {
            dto.setResult(new TaskResultDTO(
                    taskResult.getId(),
                    taskResult.getResultName(),
                    taskResult.getResultType(),
                    taskResult.getMarked()
            ));
        }else{
            dto.setResult(new TaskResultDTO(
                    0L,
                    "В работе",
                    4,
                    false
            ));
        }
        dto.setStatus(model.getStatus());
        return dto;
    }

    public TaskExecutorDtoExt toDtoExt(TaskExecutorModel model) {
        TaskExecutorDtoExt dto = new TaskExecutorDtoExt();
        dto.setId(model.getId());
        dto.setExecutor(new UserNameDTO(model.getExecutor()));
        dto.setTask(taskMapper.toDTO(model.getTask()));
        dto.setCreatedAt(options.convertDateTimeToString(model.getCreateTime()));
        dto.setPlanedDate(options.convertDateTimeToString(model.getPlanedDate()));
        dto.setDateOfCompletion(options.convertDateTimeToString(model.getDateOfComletion()));
        if(model.getComment()!=null) {
            dto.setComment(model.getComment());
        }
        TaskResultsModel taskResult = model.getResult();
        if (taskResult != null) {
            dto.setResult(new TaskResultDTO(
                    taskResult.getId(),
                    taskResult.getResultName(),
                    taskResult.getResultType(),
                    taskResult.getMarked()
            ));
        }else{
            dto.setResult(new TaskResultDTO(
                    0L,
                    "В работе",
                    4,
                    false
            ));
        }
        dto.setStatus(model.getStatus());
        return dto;
    }

    public TaskExecutorModel toModel(TaskExecutorDTO dto) {
        TaskExecutorModel model = new TaskExecutorModel();
        model.setId(dto.getId());
        model.setTask(
                tasksService.findById(dto.getTaskId()));
        model.setExecutor(
                userService.findById(dto.getExecutor().getId()));
        model.setCreateTime(options.convertStringToDateTime(dto.getCreatedAt()));
        model.setPlanedDate(options.convertStringToDateTime(dto.getPlanedDate()));
        model.setDateOfComletion(options.convertStringToDateTime(dto.getDateOfCompletion()));
        model.setComment(dto.getComment());
        if (dto.getResult() != null) {
            Long resId = dto.getResult().getId();
            Optional<TaskResultsModel> taskResult = taskResultsRepository.findById(resId);
            model.setResult(taskResult.orElseThrow(
                    () -> new ResourceNotFoundException("Не найден результат выполнения задачи с id '" + resId + "'"))
            );
        }
        model.setStatus(dto.getStatus());
        return model;
    }

    public TaskExecutorModel moveChanges(TaskExecutorModel receiver, TaskExecutorDTO source) {
        receiver.setExecutor(
                userService.findById(source.getExecutor().getId()));
        receiver.setPlanedDate(options.convertStringToDateTime(source.getPlanedDate()));
        return receiver;
    }

}
