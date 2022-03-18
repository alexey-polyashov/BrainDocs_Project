package com.braindocs.services.tasks;

import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.tasks.TaskCommentModel;
import com.braindocs.repositories.tasks.TaskCommentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCommentsService {

    private final TaskCommentsRepository taskCommentsRepository;

    public TaskCommentModel findById(Long id) {
        return taskCommentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден комментарий по id '" + id + "'"));
    }

}
