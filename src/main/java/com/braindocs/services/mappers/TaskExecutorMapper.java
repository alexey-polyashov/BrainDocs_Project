package com.braindocs.services.mappers;

import com.braindocs.dto.tasks.TaskExecutorDTO;
import com.braindocs.dto.tasks.TaskExecutorDtoExt;
import com.braindocs.dto.tasks.TaskResultDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.models.tasks.TaskResultsModel;
import com.braindocs.repositories.tasks.TaskResultsRepository;
import com.braindocs.services.OptionService;
import com.braindocs.services.tasks.TasksService;
import com.braindocs.services.users.UserService;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TaskExecutorMapper {

    private UserService userService;
    private OptionService optionService;
    private TasksService tasksService;
    private TaskResultsRepository taskResultsRepository;
    private TaskMapper taskMapper;

    @Autowired
    public TaskExecutorMapper(UserService userService,
                              OptionService optionService,
                              TaskResultsRepository taskResultsRepository,
                              TaskMapper taskMapper,
                              @Lazy TasksService tasksService) {
        this.userService = userService;
        this.optionService = optionService;
        this.tasksService = tasksService;
        this.taskResultsRepository = taskResultsRepository;
        this.taskMapper = taskMapper;
    }

    public TaskExecutorDTO toDTO(TaskExecutorModel model) {
        TaskExecutorDTO dto = new TaskExecutorDTO();
        dto.setId(model.getId());
        dto.setExecutor(new UserNameDTO(model.getExecutor()));
        dto.setTaskId(model.getTask().getId());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        LocalDateTimeStringConverter ldtc = new LocalDateTimeStringConverter(dtf,dtf);
        dto.setCreatedAt(ldtc.toString(model.getCreateTime()));
        dto.setPlanedDate(ldtc.toString(model.getPlanedDate()));
        dto.setDateOfCompletion(ldtc.toString(model.getDateOfComletion()));
        dto.setComment(model.getComment());
        TaskResultsModel taskResult = model.getResult();
        dto.setResult(new TaskResultDTO(
                taskResult.getId(),
                taskResult.getResultName(),
                taskResult.getMarked()
        ));
        dto.setStatus(model.getStatus());
        return dto;
    }

    public TaskExecutorDtoExt toDtoExt(TaskExecutorModel model) {
        TaskExecutorDtoExt dto = new TaskExecutorDtoExt();
        dto.setId(model.getId());
        dto.setExecutor(new UserNameDTO(model.getExecutor()));
        dto.setTask(taskMapper.toDTO(model.getTask()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        LocalDateTimeStringConverter ldtc = new LocalDateTimeStringConverter(dtf,dtf);
        dto.setCreatedAt(ldtc.toString(model.getCreateTime()));
        dto.setPlanedDate(ldtc.toString(model.getPlanedDate()));
        dto.setDateOfCompletion(ldtc.toString(model.getDateOfComletion()));
        dto.setComment(model.getComment());
        TaskResultsModel taskResult = model.getResult();
        if(taskResult!=null) {
            dto.setResult(new TaskResultDTO(
                    taskResult.getId(),
                    taskResult.getResultName(),
                    taskResult.getMarked()
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        LocalDateTimeStringConverter ldtc = new LocalDateTimeStringConverter(dtf,dtf);
        model.setPlanedDate(ldtc.fromString(dto.getCreatedAt()));
        if(!dto.getCreatedAt().isEmpty()){
            model.setDateOfComletion(ldtc.fromString(dto.getDateOfCompletion()));
        }
        model.setComment(dto.getComment());
        if(dto.getResult()!=null) {
            Long resId = dto.getResult().getId();
            Optional<TaskResultsModel> taskResult = taskResultsRepository.findById(resId);
            model.setResult(taskResult.orElseThrow(
                    ()->new ResourceNotFoundException("Не найден результат выполнения задачи с id '" + resId + "'"))
            );
        }
        model.setStatus(dto.getStatus());
        return model;
    }

    public TaskExecutorModel moveChanges(TaskExecutorModel receiver, TaskExecutorDTO source) {
        receiver.setExecutor(
                userService.findById(source.getExecutor().getId()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        LocalDateTimeStringConverter ldtc = new LocalDateTimeStringConverter(dtf,dtf);
        receiver.setPlanedDate(ldtc.fromString(source.getCreatedAt()));
        receiver.setDateOfComletion(ldtc.fromString(source.getDateOfCompletion()));
        receiver.setComment(source.getComment());
        Long resId = source.getResult().getId();
        Optional<TaskResultsModel> taskResult = taskResultsRepository.findById(resId);
        receiver.setResult(taskResult.orElseThrow(
                ()->new ResourceNotFoundException("Не найден результат выполнения задачи с id '" + resId + "'"))
        );
        receiver.setStatus(source.getStatus());
        return receiver;
    }

}