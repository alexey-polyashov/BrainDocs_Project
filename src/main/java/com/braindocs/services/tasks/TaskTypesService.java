package com.braindocs.services.tasks;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.tasks.TaskTypeModel;
import com.braindocs.repositories.tasks.TaskTypesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskTypesService {

    private final TaskTypesRepository taskTypesRepository;

    public List<TaskTypeModel> getTypes(MarkedRequestValue marked) {
        switch(marked){
            case ON:
                return taskTypesRepository.findByMarked(false);
            case ONLY:
                return taskTypesRepository.findByMarked(true);
            default:
                return taskTypesRepository.findAll();
        }
    }

    public TaskTypeModel findById(Long id){
        return taskTypesRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Не найден тип задачи по id '" + id + "'"));
    }

}
