package com.braindocs.services.tasks;

import com.braindocs.common.MailService;
import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.dto.tasks.*;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.models.tasks.*;
import com.braindocs.models.users.UserModel;
import com.braindocs.repositories.specifications.TaskExecutorSpecificationBuilder;
import com.braindocs.repositories.specifications.TaskSpecificationBuilder;
import com.braindocs.repositories.tasks.TaskCommentsRepository;
import com.braindocs.repositories.tasks.TaskExecutorsRepository;
import com.braindocs.repositories.tasks.TaskResultsRepository;
import com.braindocs.repositories.tasks.TasksRepository;
import com.braindocs.services.FilesService;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.documents.DocumentsService;
import com.braindocs.services.mappers.*;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
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
    private final TaskResultMapper taskResultMapper;
    private final TaskResultsRepository taskResultsRepository;
    private final FilesService filesService;
    private final FileMapper fileMapper;
    private final MailService mailService;
    private final DocumentsService documentsService;

    //получение документа по id
    public TaskModel getTask(Long taskId) {
        return tasksRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id-'" + taskId + "' не найдена"));
    }

    private void setTextAboutExecutorTask(StringBuilder mailText, TaskExecutorModel executor) {
        TaskModel task = executor.getTask();
        String header = task.getHeading();
        String content = task.getContent();
        Set<DocumentModel> subjects = task.getSubjects();
        List<TaskExecutorModel> taskExecutors = taskExecutorsRepository.findByTask(task);
        String planedDate = options.convertDateTimeToString(executor.getPlanedDate());
        mailText.append("<b>Задача: </b>").append("(").append(task.getId()).append(")").append(header).append("<br/>");
        mailText.append("<b>Описание задачи: </b>").append(content).append("<br/>");
        mailText.append("<b>Планируемое время выполнения: </b>").append(planedDate).append("<br/>");
        mailText.append("<b>Автор задачи: </b>").append(task.getAuthor().getShortname()).append("<br/><br/>");
        mailText.append("<b>Предметы задачи: </b>").append("<br/><ul>");
        subjects.stream()
                .forEach(p->mailText.append("<li>").append(p.getHeading()).append("</li>"));
        mailText.append("</ul>");
        mailText.append("<b>Назначены исполнители: </b>").append("<br/><ul>");
        taskExecutors.stream()
                .forEach(p->mailText.append("<li>").append(p.getExecutor().getShortname()).append("</li>"));
        mailText.append("</ul>");
    }

    private void setTextAboutExecuteTask(StringBuilder mailText, TaskExecutorModel executor) {
        TaskModel task = executor.getTask();
        String header = task.getHeading();
        String content = task.getContent();
        Set<DocumentModel> subjects = task.getSubjects();
        List<TaskExecutorModel> taskExecutors = taskExecutorsRepository.findByTask(task);
        String planedDate = options.convertDateTimeToString(executor.getPlanedDate());
        mailText.append("<b>Задача: </b>").append("(").append(task.getId()).append(")").append(header).append("<br/>");
        mailText.append("<b>Описание задачи: </b>").append(content).append("<br/>");
        mailText.append("<b>Планируемое время выполнения: </b>").append(planedDate).append("<br/>");
        mailText.append("<b>Предметы задачи: </b>").append("<br/><ul>");
        subjects.stream()
                .forEach(p->mailText.append("<li>").append(p.getHeading()).append("</li>"));
        mailText.append("</ul>");
        String resultColor = "color:#585ed6";
        if(executor.getResult().getResultType()==1){
            resultColor = "color:green";
        }else if(executor.getResult().getResultType()==2){
            resultColor = "color:red";
        }else if(executor.getResult().getResultType()==3){
            resultColor = "color:darkorange";
        }
        mailText.append("Выполнена исполнителем ").append("<b style =\"color:#585ed6\">")
                .append(executor.getExecutor().getShortname()).append("</b> с результатом: <b style =\""+resultColor + "\">").append(executor.getResult().getResultName()).append("</b><br/>");
        mailText.append("Комментарии к выполнению: ").append(executor.getComment());
    }

    private void setTextAboutComment(StringBuilder mailText, TaskCommentModel comment) {
        TaskModel task = comment.getTask();
        String header = task.getHeading();
        String content = task.getContent();
        mailText.append("<b>Добавлен комментарий к задаче: </b>").append("(").append(task.getId()).append(")").append(header).append("<br/>");
        mailText.append("<br/><br/>");
        mailText.append("<b><span style =\"color:#585ed6\">").append(comment.getAuthor().getShortname()).append("</span></b> написал <b>").append(options.convertDateTimeToString(comment.getCreateTime())).append("</b><br/>");
        mailText.append(comment.getComment()).append("<br/>");
    }

    @Transactional
    public Page<TaskModel> getTasksByFields(int pageNumber, int pageSize, List<SearchCriteriaDTO> filter) {

        List<SearchCriteriaDTO> markedCriteria = filter.stream()
                .filter(p -> p.getKey().equals("marked"))
                .collect(Collectors.toList());

        if (markedCriteria.isEmpty()) {
            filter.add(new SearchCriteriaDTO("marked", ":", "OFF"));
        } else {
            if (!Utils.isValidEnum(MarkedRequestValue.class,
                    markedCriteria.get(0)
                            .getValue()
                            .toUpperCase(Locale.ROOT))) {
                throw new BadRequestException("Недопустимое значение параметра marked");
            }
        }

        TaskSpecificationBuilder builder = new TaskSpecificationBuilder(userService, organisationService, taskTypesService, options);
        for (SearchCriteriaDTO creteriaDTO : filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<TaskModel> spec = builder.build();

        return tasksRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

    }

    @Transactional
    public Page<TaskExecutorModel> getExecutorsByFields(int pageNumber, int pageSize, List<SearchCriteriaDTO> filter) {

        TaskExecutorSpecificationBuilder builder = new TaskExecutorSpecificationBuilder(userService, organisationService, taskTypesService, options);
        for (SearchCriteriaDTO creteriaDTO : filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<TaskExecutorModel> spec = builder.build();

        return taskExecutorsRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

    }

    @Transactional
    public Page<TaskDTO> getTasksDTOByFields(Integer page, Integer recordsOnPage, List<SearchCriteriaDTO> filter) {
        Page<TaskModel> tasks = getTasksByFields(page, recordsOnPage, filter);
        return tasks.map(taskMapper::toDTO);
    }

    @Transactional
    public Page<TaskExecutorDtoExt> getExecutorsDTOByFields(Integer page, Integer recordsOnPage, List<SearchCriteriaDTO> filter) {
        Page<TaskExecutorModel> executors = getExecutorsByFields(page, recordsOnPage, filter);
        return executors.map(taskExecutorMapper::toDtoExt);
    }

    @Transactional
    public Long add(TaskModel taskModel, Principal author) {
        if (taskModel.getAuthor() == null) {
            if (author == null) {
                throw new BadRequestException("Автор задачи не может быть определен");
            }
            UserModel user = userService.findByUsername(
                    author.getName()).orElseThrow(() -> new ResourceNotFoundException("Пользователь по имени '" + author.getName() + "' не определен"));
            taskModel.setAuthor(user);
        }
        taskModel = tasksRepository.save(taskModel);
        return taskModel.getId();
    }

    public TaskModel findById(Long taskId) {
        return getTask(taskId);
    }

    @Transactional
    public TaskDTO findDTOById(Long taskId) {
        return taskMapper.toDTO(findById(taskId));
    }

    @Transactional
    public Long saveTask(TaskDTO taskDTO) {
        TaskModel taskModel = findById(taskDTO.getId());
        taskMapper.moveChanges(taskModel, taskDTO);
        return taskModel.getId();
    }

    @Transactional
    public Long saveExecutor(TaskExecutorDTO taskExecutorDTO) {

        Long entId = taskExecutorDTO.getId();
        TaskExecutorModel executor = taskExecutorsRepository.findById(
                        entId)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден исполнитель по id '" + entId + "'"));
        taskExecutorMapper.moveChanges(executor, taskExecutorDTO);
        taskExecutorsRepository.save(executor);
        setTaskStatusByActiveExecutors(taskExecutorDTO.getTaskId());

        StringBuilder mailText = new StringBuilder("");
        setTextAboutExecutorTask(mailText, executor);
        mailService.sendHTMLEmail(executor.getExecutor().getEmail(),
                mailText,
                "Task changed");

        return executor.getId();

    }

    @Transactional
    public void deleteTask(Long taskId) {
        TaskModel task = getTask(taskId);
        tasksRepository.delete(task);
    }

    @Transactional
    public void markTask(Long taskId) {
        TaskModel task = getTask(taskId);
        task.setMarked(true);
    }

    @Transactional
    public void unMarkTask(Long taskId) {
        TaskModel task = getTask(taskId);
        task.setMarked(false);
    }

    public List<TaskExecutorModel> getExecutors(Long taskId) {
        TaskModel task = findById(taskId);
        return taskExecutorsRepository.findByTask(task);
    }

    @Transactional
    public Long addExecutor(TaskExecutorModel taskExecutorModel) {

        taskExecutorModel.setStatus(1L);
        taskExecutorModel = taskExecutorsRepository.save(taskExecutorModel);
        setTaskStatusByActiveExecutors(taskExecutorModel.getTask().getId());

        StringBuilder mailText = new StringBuilder("");
        setTextAboutExecutorTask(mailText, taskExecutorModel);
        mailService.sendHTMLEmail(taskExecutorModel.getExecutor().getEmail(),
                mailText,
                "New task");

        return taskExecutorModel.getId();

    }

    @Transactional
    public TaskExecutorModel getExecutor(Long taskId, Long exId) {
        TaskModel task = findById(taskId);
        return taskExecutorsRepository.findByTaskAndId(task, exId)
                .orElseThrow(() -> new ResourceNotFoundException("Исполнитель по id '" + exId + "' не найден"));
    }

    @Transactional
    public TaskExecutorDTO getExecutorDTO(Long taskId, Long exId) {
        return taskExecutorMapper.toDTO(getExecutor(taskId, exId));
    }

    @Transactional
    public void deleteExecutor(Long taskId, Long exId) {
        TaskModel task = getTask(taskId);
        taskExecutorsRepository.deleteByTaskAndId(task, exId);
        setTaskStatusByActiveExecutors(taskId);
    }

    @Transactional
    public Long executeTask(Long taskId, Long execotorId, TaskExecutorResultDTO executorResult) {
        TaskExecutorModel tem = taskExecutorsRepository.findById(execotorId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача исполнителя по id - '" + execotorId + "'"));
        Long resId = executorResult.getResultId();
        Long taskTypeId = tem.getTask().getType().getId();
        //проверка доступности указанного результата выполнения для задачи
        TaskResultsModel trm = getResulByIdtForTaskType(
                resId,
                taskTypeId
        ).orElseThrow(()->new ResourceNotFoundException("Не найден результат с id '" + resId + "' для типа задач с id '" + taskTypeId + "'"));
        tem.setDateOfComletion(options.convertStringToDateTime(executorResult.getExecuteDateTime()));
        tem.setComment(executorResult.getResultComment());
        tem.setStatus(3L);
        tem.setResult(trm);
        taskExecutorsRepository.save(tem);
        setTaskStatusByActiveExecutors(taskId);
        StringBuilder mailText = new StringBuilder("");
        setTextAboutExecuteTask(mailText, tem);
        mailService.sendHTMLEmail(tem.getTask().getAuthor().getEmail(),
                mailText,
                "Execute task");
        return tem.getId();
    }

    @Transactional
    public Long canselTask(Long taskId, Long execotorId) {
        TaskExecutorModel tem = taskExecutorsRepository.findById(execotorId)
                .orElseThrow(()->new ResourceNotFoundException("Не найдена задача исполнителя по id - '" + execotorId + "'"));
        tem.setStatus(4L);
        taskExecutorsRepository.save(tem);
        //установка конечного статуса у задачи - выполнена, если нет больше исполнителей
        setTaskStatusByActiveExecutors(taskId);
        return tem.getId();
    }

    public List<TaskCommentModel> getComments(Long taskId) {
        TaskModel task = findById(taskId);
        return taskCommentsRepository.findByTask(task);
    }

    @Transactional
    public Long addComment(TaskCommentModel taskCommentModel, Principal author) {
        if (taskCommentModel.getAuthor() == null) {
            if (author == null) {
                throw new BadRequestException("Автор комментария не может быть определен");
            }
            UserModel user = userService.findByUsername(
                    author.getName()).orElseThrow(() -> new ResourceNotFoundException("Пользователь по имени '" + author.getName() + "' не определен"));
            taskCommentModel.setAuthor(user);
        }
        taskCommentModel = taskCommentsRepository.save(taskCommentModel);

        StringBuilder mailText = new StringBuilder("");
        setTextAboutComment(mailText, taskCommentModel);
        mailService.sendHTMLEmail(taskCommentModel.getTask().getAuthor().getEmail(),
                mailText,
                "New comment");

        return taskCommentModel.getId();
    }

    @Transactional
    public Long saveComment(TaskCommentDTO taskCommentDTO) {
        Long entId = taskCommentDTO.getId();
        TaskCommentModel taskComment = taskCommentsRepository.findById(
                        entId)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден по id '" + entId + "'"));
        taskCommentMapper.moveChanges(taskComment, taskCommentDTO);
        return taskComment.getId();
    }

    public TaskCommentModel getComment(Long taskId, Long commentId) {
        TaskModel task = findById(taskId);
        return taskCommentsRepository.findByTaskAndId(task, commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий по id '" + commentId + "' не найден"));
    }

    @Transactional
    public void deleteComment(Long taskId, Long commentId) {
        TaskModel task = getTask(taskId);
        taskCommentsRepository.deleteByTaskAndId(task, commentId);
    }

    //получение файла по id
    @Transactional
    public FileModel getTaskFile(Long taskId, Long fileId) {
        TaskModel taskModel = getTask(taskId);
        FileModel fileModel = null;
        for (FileModel file : taskModel.getFiles()) {
            if (file.getId().equals(fileId)) {
                fileModel = file;
                break;
            }
        }
        if (fileModel == null) {
            throw new ResourceNotFoundException("Файл с id-'" + fileId + "' не принадлежит задаче с id-'" + taskId + "'");
        }
        return fileModel;
    }

    //добавление одного файла
    @Transactional
    public FileDTO addFile(Long taskId, FileModel file, MultipartFile fileData) throws IOException {
        if (file.getId() != 0) {
            log.error("file id is not empty");
            throw new BadRequestException("Id файла должен быть пустым");
        }
        TaskModel taskModel = getTask(taskId);
        FileModel fileModel = filesService.add(file, fileData);
        taskModel.getFiles().add(fileModel);
        tasksRepository.save(taskModel);
        return fileMapper.toDTO(fileModel);
    }

    @Transactional
    public Set<FileModel> getFilesList(Long taskId) {
        TaskModel taskModel = getTask(taskId);
        return taskModel.getFiles();
    }

    @Transactional
    public Set<FileDTO> getFilesDTOList(Long taskId, BiConsumer<FileDTO, Long> setLink) {
        TaskModel taskModel = getTask(taskId);
        return taskModel.getFiles().stream().map(
                p -> {
                    FileDTO res = fileMapper.toDTO(p);
                    setLink.accept(res, taskId);
                    return res;
                }
        ).collect(Collectors.toSet());
    }

    //получение описания файла по id
    @Transactional
    public FileModel getFileDescribe(Long taskId, Long fileId) {
        getTaskFile(taskId, fileId);
        return filesService.findById(fileId);
    }

    @Transactional
    public FileDTO getFileDTODescribe(Long docId, Long fileId, BiConsumer<FileDTO, Long> setLink) {
        FileDTO fDTO = fileMapper.toDTO(filesService.findById(fileId));
        setLink.accept(fDTO, docId);
        return fDTO;
    }

    //получение данных файла по id
    @Transactional
    public FileDataDTO getFileData(Long taskId, Long fileId) {
        getTaskFile(taskId, fileId);
        return fileMapper.toDTOwithData(
                filesService.getFileData(fileId)
        );
    }

    //добавление одного файла
    @Transactional
    public FileDTO changeFile(Long taskId, FileModel file, MultipartFile fileData) throws IOException {
        if (file.getId() == null || file.getId() == 0) {
            log.error("file 'id' is empty");
            throw new BadRequestException("Не определен 'id' изменяемого файла");
        }
        getTaskFile(taskId, file.getId()); //проверка существования файла
        FileModel fileModel = null;
        if (fileData == null || fileData.isEmpty()) {
            fileModel = filesService.saveOnlyDescribe(file);
        } else {
            fileModel = filesService.saveWithAllData(file, fileData);
        }
        return fileMapper.toDTO(fileModel);
    }

    //удаление файла по id
    @Transactional
    public void deleteFile(Long taskId, Long fileId) {
        TaskModel taskModel = getTask(taskId);
        FileModel fileModel = getTaskFile(taskId, fileId);
        filesService.delete(fileId);
        taskModel.getFiles().remove(fileModel);
        tasksRepository.save(taskModel);
    }

    //удаление всех файлов
    @Transactional
    public void clearFiles(Long taskId) {
        TaskModel taskModel = getTask(taskId);
        for (FileModel file : taskModel.getFiles()) {
            filesService.delete(file.getId());
        }
        taskModel.getFiles().clear();
        tasksRepository.save(taskModel);
    }


    public List<TaskResultDTO> getResultListForTaskType(Long typeId) {
        return taskResultsRepository.findByTaskTypeId(typeId)
                .stream()
                .map(taskResultMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Optional<TaskResultsModel> getResulByIdtForTaskType(Long id, Long typeId) {
        TaskTypeModel ttm = taskTypesService.findById(typeId);
        return taskResultsRepository.findByIdAndTaskType(id, ttm);
    }

    private void setTaskStatusByActiveExecutors(Long taskId){
        //установка конечного статуса у задачи - выполнена, если нет больше исполнителей
        TaskModel task = tasksRepository.getById(taskId);
        List<TaskExecutorModel> taskExecutors = taskExecutorsRepository.findActiveExecutorsInTask(task);
        if(taskExecutors.isEmpty()){
            task.setStatus(2L);
        }else{
            task.setStatus(1L);
        }
        tasksRepository.save(task);
    }

    @Transactional
    public List<TaskDTO> getTaskListForDocument(Long docId) {
        //DocumentModel doc = documentsService.getDocument(docId);
        List<TaskModel> tm = tasksRepository.findForDocument(docId);
        return tm.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }
}
