package com.braindocs.services.mappers;

import com.braindocs.dto.tasks.TaskTypeDTO;
import com.braindocs.models.tasks.TaskTypeModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskTypeMapper {

    private final ModelMapper modelMapper;

    public TaskTypeDTO toDTO(TaskTypeModel model) {
        return modelMapper.map(model, TaskTypeDTO.class);
    }

    public TaskTypeModel toModel(TaskTypeDTO dto) {
        return modelMapper.map(dto, TaskTypeModel.class);
    }
}
