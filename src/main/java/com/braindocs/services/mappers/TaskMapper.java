package com.braindocs.services.mappers;

import com.braindocs.dto.documents.DocumentDTO;
import com.braindocs.dto.tasks.TaskDTO;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.tasks.TaskModel;
import com.braindocs.services.OptionService;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final UserService userService;
    private final FileMapper fileMapper;
    private final OptionService optionService;

    public TaskDTO toDTO(DocumentModel docModel) {

        TaskDTO dto = new TaskDTO();
        return dto;

    }

    public TaskModel toModel(DocumentDTO docDTO) throws ParseException {

        TaskModel docModel = new TaskModel();

        return docModel;
    }


}
