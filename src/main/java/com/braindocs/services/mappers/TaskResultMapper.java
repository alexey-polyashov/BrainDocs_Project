package com.braindocs.services.mappers;

import com.braindocs.dto.tasks.TaskExecutorDTO;
import com.braindocs.dto.tasks.TaskResultDTO;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.models.tasks.TaskResultsModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TaskResultMapper {

    public TaskResultDTO toDTO(TaskResultsModel model) {
        return new TaskResultDTO(
                model.getId(),
                model.getResultName(),
                model.getResultType(),
                model.getMarked()
        );
    }

    public TaskResultsModel toModel(TaskResultDTO dto) {
        TaskResultsModel model = new TaskResultsModel();
        model.setResultType(dto.getResultType());
        model.setId(dto.getId());
        model.setResultName(dto.getResultName());
        model.setMarked(false);
        return model;
    }

    public TaskResultsModel moveChanges(TaskResultsModel receiver, TaskResultDTO source) {
        receiver.setResultType(source.getResultType());
        receiver.setResultName(source.getResultName());
        return receiver;
    }

}
