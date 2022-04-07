package com.braindocs.services.mappers;

import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.tasks.TaskDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.models.files.FileModel;
import com.braindocs.models.tasks.TaskModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.documents.DocumentsService;
import com.braindocs.services.tasks.TaskTypesService;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskTypeMapper taskTypeMapper;
    private final TaskTypesService taskTypesService;
    private final DocumentMapper documentMapper;
    private final DocumentsService documentsService;
    private final UserService userService;
    private final Options options;
    private final FileMapper fileMapper;

    public TaskDTO toDTO(TaskModel model) {

        TaskDTO dto = new TaskDTO();
        dto.setId(model.getId());
        dto.setCreateTime(options.convertDateTimeToString(model.getCreateTime()));
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

        dto.setFiles(model.getFiles().stream().map(fileMapper::toDTO).collect(Collectors.toList()));

        return dto;

    }

    public TaskModel toModel(TaskDTO dto) {

        TaskModel model = new TaskModel();
        model.setId(dto.getId());
        model.setCreateTime(options.convertStringToDateTime(dto.getCreateTime()));
        model.setType(
                taskTypesService.findById(dto.getTaskType().getId()));
        model.setHeading(dto.getHeading());
        model.setContent(dto.getContent());
        model.setStatus(1L);
        UserModel userModel = userService.findById(dto.getAuthor().getId());
        model.setAuthor(userModel);
        model.setMarked(false);

        model.setSubjects(Utils.emptyIfNull(dto.getSubjects())
                .stream()
                .map(p -> documentsService.getDocument(p.getId()))
                .collect(Collectors.toSet()));
        model.setFiles(new HashSet<FileModel>());

        return model;

    }

    public TaskModel moveChanges(TaskModel receiver, TaskDTO source) {
        receiver.setCreateTime(options.convertStringToDateTime(source.getCreateTime()));
        receiver.setType(
                taskTypesService.findById(source.getTaskType().getId()));
        receiver.setHeading(source.getHeading());
        receiver.setContent(source.getContent());
        //receiver.setStatus(source.getStatus());
        UserModel userModel = userService.findById(source.getAuthor().getId());
        receiver.setAuthor(userModel);
        return receiver;
    }

}
