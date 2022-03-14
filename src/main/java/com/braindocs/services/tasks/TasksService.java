package com.braindocs.services.tasks;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.tasks.TaskCommentDTO;
import com.braindocs.dto.tasks.TaskDTO;
import com.braindocs.dto.tasks.TaskExecutorDTO;
import com.braindocs.dto.tasks.TaskExecutorDtoExt;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.tasks.TaskCommentModel;
import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.models.tasks.TaskModel;
import com.braindocs.repositories.specifications.TaskSpecificationBuilder;
import com.braindocs.repositories.tasks.TaskCommentsRepository;
import com.braindocs.repositories.tasks.TaskExecutorsRepository;
import com.braindocs.repositories.tasks.TasksRepository;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.mappers.TaskCommentMapper;
import com.braindocs.services.mappers.TaskExecutorMapper;
import com.braindocs.services.mappers.TaskMapper;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TasksService {

    private final UserService userService;
    private final TaskTypesService taskTypesService;
    private final TaskExecutorsRepository taskExecutorsRepository;
    private final TaskCommentsRepository taskCommentsRepository;
    private final TaskMapper taskMapper;
    private final Options options;
    private final TasksRepository tasksRepository;
    private final OrganisationService organisationService;
    private final TaskExecutorMapper taskExecutorMapper;
    private final TaskCommentMapper taskCommentMapper;

    @Transactional
    public Page<TaskModel> getTasksByFields(int pageNumber, int pageSize, List<SearchCriteriaDTO> filter){

        List<SearchCriteriaDTO> markedCriteria = filter.stream()
                .filter(p->p.getKey().equals("marked"))
                .collect(Collectors.toList());

        if(markedCriteria.isEmpty()){
            filter.add(new SearchCriteriaDTO("marked", ":", "OFF"));
        }else{
            if(!Utils.isValidEnum(MarkedRequestValue.class,
                    markedCriteria.get(0)
                            .getValue()
                            .toUpperCase(Locale.ROOT))){
                throw new BadRequestException("Недопустимое значение параметра marked");
            }
        }

        TaskSpecificationBuilder builder = new TaskSpecificationBuilder(userService, organisationService, taskTypesService, options);
        for(SearchCriteriaDTO creteriaDTO: filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<TaskModel> spec = builder.build();

        return tasksRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

    }

    @Transactional
    public Page<TaskDTO> getTasksDTOByFields(Integer page, Integer recordsOnPage, List<SearchCriteriaDTO> filter) {
        Page<TaskModel> documents = getTasksByFields(page, recordsOnPage, filter);
        return documents.map(taskMapper::toDTO);
    }

    public Long add(TaskModel taskModel) {
        taskModel = tasksRepository.save(taskModel);
        return taskModel.getId();
    }

    public TaskModel findById(Long taskId){
        return tasksRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача по id '" + taskId + "'"));
    }

    public Long saveTask(TaskDTO taskDTO) {
        TaskModel taskModel = findById(taskDTO.getId());
        taskMapper.moveChanges(taskModel, taskDTO);
        return taskModel.getId();
    }

    public Long addExecutor(TaskExecutorModel taskExecutorModel) {
        taskExecutorModel = taskExecutorsRepository.save(taskExecutorModel);
        return taskExecutorModel.getId();
    }

    @Transactional
    public Long saveExecutor(TaskExecutorDTO taskExecutorDTO) {
        Long entId = taskExecutorDTO.getId();
        TaskExecutorModel executor = taskExecutorsRepository.findById(
                        entId)
                .orElseThrow(()->new ResourceNotFoundException("Не найден исполнитель по id '" + entId + "'"));
        taskExecutorMapper.moveChanges(executor, taskExecutorDTO);
        return executor.getId();
    }

    public Long addComment(TaskCommentModel taskCommentModel) {
        taskCommentModel = taskCommentsRepository.save(taskCommentModel);
        return taskCommentModel.getId();
    }

    @Transactional
    public Long saveComment(TaskCommentDTO taskCommentDTO) {
        Long entId = taskCommentDTO.getId();
        TaskCommentModel taskComment = taskCommentsRepository.findById(
                        entId)
                .orElseThrow(()->new ResourceNotFoundException("Комментарий не найден по id '" + entId + "'"));
        taskCommentMapper.moveChanges(taskComment, taskCommentDTO);
        return taskComment.getId();
    }

    public List<TaskExecutorModel> getExecutors(Long taskId) {
        TaskModel task = findById(taskId);
        return taskExecutorsRepository.findByTask(task);
    }

    public TaskExecutorModel getExecutor(Long taskId, Long exId) {
        TaskModel task = findById(taskId);
        return taskExecutorsRepository.findByTaskAndId(task, exId)
                .orElseThrow(()->new ResourceNotFoundException("Исполнитель по id '" + exId + "' не найден"));
    }

    public List<TaskCommentModel> getComments(Long taskId) {
        TaskModel task = findById(taskId);
        return taskCommentsRepository.findByTask(task);
    }

    public TaskCommentModel getComment(Long taskId, Long commentId) {
        TaskModel task = findById(taskId);
        return taskCommentsRepository.findByTaskAndId(task, commentId)
                .orElseThrow(()->new ResourceNotFoundException("Комментарий по id '" + commentId + "' не найден"));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        TaskModel task = tasksRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача по id '" + taskId + "'"));
        tasksRepository.delete(task);
    }

    @Transactional
    public void markTask(Long taskId) {
        TaskModel task = tasksRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача по id '" + taskId + "'"));
        task.setMarked(true);
    }

    @Transactional
    public void unMarkTask(Long taskId) {
        TaskModel task = tasksRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача по id '" + taskId + "'"));
        task.setMarked(false);
    }

    @Transactional
    public void deleteComment(Long taskId, Long commentId) {
        TaskModel task = tasksRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Не найден задача по id '" + taskId + "'"));
        taskCommentsRepository.deleteByTaskAndId(task, commentId);
    }

    @Transactional
    public void deleteExecutor(Long taskId, Long exId) {
        TaskModel task = tasksRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача по id '" + taskId + "'"));
        taskExecutorsRepository.deleteByTaskAndId(task, exId);
    }

    public Page<TaskExecutorDtoExt> getExecutorsDTOByFields(Integer page, Integer recordsOnPage, List<SearchCriteriaDTO> filter) {
        return null;
    }
}
