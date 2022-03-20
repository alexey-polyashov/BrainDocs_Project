package com.braindocs.services.mappers;

import com.braindocs.common.Utils;
import com.braindocs.dto.tasks.TaskDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.models.tasks.TaskModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.OptionService;
import com.braindocs.services.documents.DocumentsService;
import com.braindocs.services.tasks.TaskTypesService;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskTypeMapper taskTypeMapper;
    private final TaskTypesService taskTypesService;
    private final DocumentMapper documentMapper;
    private final DocumentsService documentsService;
    private final UserService userService;
    private final OptionService optionService;

    public TaskDTO toDTO(TaskModel model) {

        TaskDTO dto = new TaskDTO();
        dto.setId(model.getId());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        dto.setCreateTime(dtf.format(model.getCreateTime()));
        dto.setTaskType(taskTypeMapper.toDTO(model.getType()));
        dto.setHeading(model.getHeading());
        dto.setContent(model.getContent());
        dto.setStatus(model.getStatus());
        dto.setAuthor(new UserNameDTO(model.getAuthor()));
        dto.setMarked(model.getMarked());
        dto.setSubjects(Utils.emptyIfNull(model.getSubjects())
                .stream()
                .map(documentMapper::toSubjectDTO)
                .collect(Collectors.toSet()));
        return dto;

    }

    public TaskModel toModel(TaskDTO dto) {

        TaskModel model = new TaskModel();
        model.setId(dto.getId());
        if(dto.getCreateTime().isEmpty()){
            model.setCreateTime(LocalDateTime.now());
        }else{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
            model.setCreateTime(LocalDateTime.parse(dto.getCreateTime(), dtf));
        }
        model.setType(
                taskTypesService.findById(dto.getTaskType().getId()));
        model.setHeading(dto.getHeading());
        model.setContent(dto.getContent());
        model.setStatus(dto.getStatus());
        UserModel userModel = userService.findById(dto.getAuthor().getId());
        model.setAuthor(userModel);
        model.setMarked(false);

        model.setSubjects(Utils.emptyIfNull(dto.getSubjects())
                .stream()
                .map(p -> documentsService.getDocument(p.getId()))
                .collect(Collectors.toSet()));

        return model;

    }

    public TaskModel moveChanges(TaskModel receiver, TaskDTO source) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(optionService.getDateTimeFormat());
        receiver.setCreateTime(LocalDateTime.parse(source.getCreateTime(), dtf));
        receiver.setType(
                taskTypesService.findById(source.getTaskType().getId()));
        receiver.setHeading(source.getHeading());
        receiver.setContent(source.getContent());
        receiver.setStatus(source.getStatus());
        UserModel userModel = userService.findById(source.getAuthor().getId());
        receiver.setAuthor(userModel);
        return receiver;
    }

}
