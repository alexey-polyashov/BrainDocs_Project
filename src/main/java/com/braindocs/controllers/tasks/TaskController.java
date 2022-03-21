package com.braindocs.controllers.tasks;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.FieldsListDTO;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.SearchCriteriaListDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.dto.files.NewFileDTO;
import com.braindocs.dto.tasks.*;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ServiceError;
import com.braindocs.models.files.FileModel;
import com.braindocs.models.tasks.TaskCommentModel;
import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.models.tasks.TaskModel;
import com.braindocs.services.mappers.*;
import com.braindocs.services.tasks.TaskTypesService;
import com.braindocs.services.tasks.TasksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tasks")
@Slf4j
@Api(value = "TaskController", tags = "Контролер задач")
public class TaskController {

    private static final String STRING_TYPE = "String";
    private static final String LONG_TYPE = "Long";
    private static final String DATE_TYPE = "Date";
    private final Options options;
    private final TasksService tasksService;
    private final TaskTypesService taskTypesService;
    private final TaskExecutorMapper taskExecutorMapper;
    private final TaskMapper taskMapper;
    private final TaskTypeMapper taskTypeMapper;
    private final TaskCommentMapper taskCommentMapper;
    private final FileMapper fileMapper;

    private void setLinkToFile(FileDTO fileDTO, Long docId) {
        fileDTO.setLink("/api/v1/tasks/" + docId + "/files/" + fileDTO.getId() + "/data");
    }


    @GetMapping(value = "/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields() {
        log.info("TaskController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Вид задачи", "type", "tasks/types", Arrays.asList(":"), LONG_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Заголовок", "heading", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Содержание", "content", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Статус", "status", "tasks/statuses", Arrays.asList(":"), LONG_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Автор", "author", "users", Arrays.asList(":"), LONG_TYPE, false));
        log.info("TaskController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @GetMapping(value = "/executors/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getExecutorFields() {
        log.info("TaskController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Комментарий", "comment", "", Arrays.asList(":"), STRING_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Статус", "status", "tasks/statuses", Arrays.asList(":"), LONG_TYPE, false));
        fieldsSet.add(new FieldsListDTO("Исполнитель", "executor", "users", Arrays.asList(":"), LONG_TYPE, false));
        log.info("TaskController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }

    @GetMapping(value = "/statuses")
    public Map<Integer, String> getStatusList() {
        log.info("TaskController: getStatuslist ");
        return new HashMap<Integer, String>() {{
            put(1, "Активна");
            put(2, "Выполнена");
            put(3, "Отменена");
        }};
    }

    @GetMapping(value = "/types/{typeId}/results")
    public List<TaskResultDTO> getResultListForTaskType(@PathVariable(name = "typeId") Long id) {
        log.info("TaskController: getStatuslist ");
        return tasksService.getResultListForTaskType(id);
    }

    @GetMapping(value = "/executors/statuses")
    public Map<Integer, String> getExecutorsStatusList() {
        log.info("TaskController: getExecutorsStatusList ");
        return new HashMap<Integer, String>() {{
            put(1, "Ожидает выполнения");
            put(2, "В работе");
            put(3, "Выполнена");
            put(4, "Отменена");
            put(5, "Уточнение");
        }};
    }

    @GetMapping(value = "/types")
    public List<TaskTypeDTO> getTypesList(
            @RequestParam(name = "marked", defaultValue = "off", required = false) String marked) {
        log.info("TaskController: getTypesList, marked -{} ", marked);
        if (!Utils.isValidEnum(MarkedRequestValue.class, marked.toUpperCase(Locale.ROOT))) {
            throw new BadRequestException("Недопустимое значение параметра marked");
        }
        return taskTypesService.getTypes(MarkedRequestValue.valueOf(marked.toUpperCase(Locale.ROOT)))
                .stream()
                .map(taskTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{taskId}")
    public TaskDTO getTask(@PathVariable("taskId") Long taskId) {
        log.info("TaskController: getTask, taskId - {} ", taskId);
        return taskMapper.toDTO(tasksService.findById(taskId));
    }

    @PostMapping(value = "/search")
    public Page<TaskDTO> getTaskList(@RequestBody SearchCriteriaListDTO requestDTO) {
        log.info("TaskController: getTaskList");
        List<SearchCriteriaDTO> filter = requestDTO.getFilter();
        Integer page = requestDTO.getPage();
        Integer recordsOnPage = requestDTO.getRecordsOnPage();
        Page<TaskDTO> taskDtoPages = tasksService.getTasksDTOByFields(
                page,
                recordsOnPage,
                filter
        );
        log.info("TaskController: getTaskList return {} elements", taskDtoPages.getSize());
        return taskDtoPages;
    }

    @PostMapping(value = "/executors/search")
    public Page<TaskExecutorDtoExt> searchExecutors(@RequestBody SearchCriteriaListDTO requestDTO) {
        log.info("TaskController: getTaskList");
        List<SearchCriteriaDTO> filter = requestDTO.getFilter();
        Integer page = requestDTO.getPage();
        Integer recordsOnPage = requestDTO.getRecordsOnPage();
        Page<TaskExecutorDtoExt> TaskExecutorDtoExtPages = tasksService.getExecutorsDTOByFields(
                page,
                recordsOnPage,
                filter
        );
        log.info("TaskController: getTaskList return {} elements", TaskExecutorDtoExtPages.getSize());
        return TaskExecutorDtoExtPages;
    }

    @PostMapping(value = "")
    public Long addTask(@Valid @RequestBody TaskDTO taskDTO, Principal principal) {
        log.info("TaskController: addTask");
        if (taskDTO.getId() != null && (taskDTO.getId() != 0)) {
            throw new BadRequestException("При добавлении нового объекта id должен быть пустым");
        }
        TaskModel taskModel = taskMapper.toModel(taskDTO);
        Long taskId = tasksService.add(taskModel, principal);
        log.info("TaskController: addTask (return id {})", taskId);
        return taskId;
    }

    @PostMapping(value = "/{taskId}")
    public Long changeTask(@PathVariable("taskId") Long taskId, @Valid @RequestBody TaskDTO taskDTO) {
        log.info("TaskController: changeTask, taskId {}", taskId);
        if (taskId == 0) {
            throw new BadRequestException("id не должен быть пустым");
        }
        taskDTO.setId(taskId);
        Long resId = tasksService.saveTask(taskDTO);
        log.info("TaskController: changeTask (return id {})", resId);
        return resId;
    }

    @DeleteMapping(value = "/{taskId}")
    public void markTask(@PathVariable("taskId") Long taskId) {
        log.info("TaskController: markTask, taskId {}", taskId);
        if (taskId == 0) {
            throw new BadRequestException("id не должен быть пустым");
        }
        tasksService.markTask(taskId);
    }

    @PostMapping(value = "/unmark/{taskId}")
    public void unMarkTask(@PathVariable("taskId") Long taskId) {
        log.info("TaskController: unMarkTask, taskId {}", taskId);
        if (taskId == 0) {
            throw new BadRequestException("id не должен быть пустым");
        }
        tasksService.unMarkTask(taskId);
    }

    @DeleteMapping(value = "/finally/{taskId}")
    public void deleteTask(@PathVariable("taskId") Long taskId) {
        log.info("TaskController: deleteTask, taskId {}", taskId);
        if (taskId == 0) {
            throw new BadRequestException("id не должен быть пустым");
        }
        tasksService.deleteTask(taskId);
    }

    @GetMapping(value = "/{taskId}/executors")
    public List<TaskExecutorDTO> getExecutors(@PathVariable("taskId") Long taskId) {
        log.info("TaskController: getExecutors");
        List<TaskExecutorModel> executors = tasksService.getExecutors(taskId);
        log.info("TaskController: getExecutors, elements - {})", executors.size());
        return executors.stream()
                .map(taskExecutorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{taskId}/executors/{exId}")
    public TaskExecutorDTO getExecutor(@PathVariable("taskId") Long taskId,
                                       @PathVariable("exId") Long exId) {
        log.info("TaskController: getExecutor");
        TaskExecutorModel executor = tasksService.getExecutor(taskId, exId);
        log.info("TaskController: getExecutor (return id {})", executor.getId());
        return taskExecutorMapper.toDTO(executor);
    }

    @PostMapping(value = "/{taskId}/executors/{exId}/results/")
    public Long executeTask(@PathVariable("exId") Long id,
                                       @PathVariable("taskId") Long taskId,
                                       @RequestBody TaskExecutorResultDTO executorResult) {
        log.info("TaskController: executeTask");
        Long executorId = tasksService.executeTask(taskId, id, executorResult);
        log.info("TaskController: executeTask (return id {})", executorId);
        return executorId;
    }

    @GetMapping(value = "/{taskId}/comments")
    public List<TaskCommentDTO> getComments(@PathVariable("taskId") Long taskId) {
        log.info("TaskController: addComments");
        List<TaskCommentModel> comments = tasksService.getComments(taskId);
        log.info("TaskController: addComments, elements - {})", comments.size());
        return comments.stream()
                .map(taskCommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{taskId}/comments/{commentId}")
    public TaskCommentDTO getComment(@PathVariable("taskId") Long taskId,
                                     @PathVariable("commentId") Long commentId) {
        log.info("TaskController: getComment");
        TaskCommentModel comment = tasksService.getComment(taskId, commentId);
        log.info("TaskController: getComment (return id {})", comment.getId());
        return taskCommentMapper.toDTO(comment);
    }

    @PostMapping(value = "/{taskId}/executors")
    public Long addExecutor(@PathVariable("taskId") Long taskId,
                            @Valid @RequestBody TaskExecutorDTO executorDTO) {
        log.info("TaskController: addExecutor");
        if (executorDTO.getId() != null && (executorDTO.getId() != 0)) {
            throw new BadRequestException("При добавлении нового объекта id должен быть пустым");
        }
        executorDTO.setTaskId(taskId);
        executorDTO.setComment("");
        executorDTO.setDateOfCompletion("");
        executorDTO.setResult(null);
        TaskExecutorModel taskExecutorModel = taskExecutorMapper.toModel(executorDTO);
        Long executorId = tasksService.addExecutor(taskExecutorModel);
        log.info("TaskController: addExecutor (return id {})", executorId);
        return executorId;
    }

    @PostMapping(value = "/{taskId}/executors/{exId}")
    public Long changeExecutor(@PathVariable("taskId") Long taskId,
                               @PathVariable("exId") Long exId,
                               @Valid @RequestBody TaskExecutorDTO executorDTO) {
        log.info("TaskController: changeExecutor");
        if (exId == 0) {
            throw new BadRequestException("id не должен быть пустым");
        }
        executorDTO.setTaskId(taskId);
        executorDTO.setId(exId);
        Long executorId = tasksService.saveExecutor(executorDTO);
        log.info("TaskController: changeExecutor (return id {})", executorId);
        return executorId;
    }

    @PostMapping(value = "/{taskId}/comments")
    public Long addComment(@PathVariable("taskId") Long taskId,
                           @Valid @RequestBody TaskCommentDTO commentDTO,
                           Principal principal) {
        log.info("TaskController: addComment");
        if (commentDTO.getId() != null && (commentDTO.getId() != 0)) {
            throw new BadRequestException("При добавлении нового объекта id должен быть пустым");
        }
        commentDTO.setTaskId(taskId);
        TaskCommentModel commentModel = taskCommentMapper.toModel(commentDTO);
        Long commentId = tasksService.addComment(commentModel, principal);
        log.info("TaskController: addComment (return id {})", commentId);
        return commentId;
    }

    @PostMapping(value = "/comments/{commentId}")
    public Long changeComment(@PathVariable("taskId") Long taskId,
                              @PathVariable("exId") Long exId,
                              @Valid @RequestBody TaskCommentDTO commentDTO) {
        log.info("TaskController: changeComment");
        if (exId == 0) {
            throw new BadRequestException("id не должен быть пустым");
        }
        commentDTO.setTaskId(taskId);
        commentDTO.setId(exId);
        Long executorId = tasksService.saveComment(commentDTO);
        log.info("TaskController: changeComment (return id {})", executorId);
        return executorId;
    }

    @DeleteMapping(value = "/{taskId}/comments/{commentId}")
    public void deleteComment(@PathVariable("taskId") Long taskId,
                              @PathVariable("commentId") Long commentId) {
        log.info("TaskController: deleteComment, taskId - {}, commentId - {}", taskId, commentId);
        tasksService.deleteComment(taskId, commentId);
    }

    @DeleteMapping(value = "/{taskId}/executors/{exId}")
    public void deleteExecutor(@PathVariable("taskId") Long taskId,
                               @PathVariable("exId") Long exId) {
        log.info("TaskController: deleteExecutor, taskId - {}, exId - {}", taskId, exId);
        tasksService.deleteExecutor(taskId, exId);
    }



    @PostMapping(value = "/{taskId}/files/upload",
            consumes = {"multipart/form-data"})
    public FileDTO uploadFile(@PathVariable("taskId") Long id, @RequestPart("fileDescribe") String jsonDescribe,
                              @RequestPart("file") MultipartFile fileData) throws IOException {
        log.info("TaskController: uploadfile");
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        if (fileData.isEmpty()) {
            log.info("Нет данных файла");
            throw new BadRequestException("Нет данных файла");
        }
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("Ошибка получения данных файла\n" + e.getMessage() + "\n" + e.getCause());
            throw new ServiceError("Ошибка получения данных файла");
        }
        FileDTO fileDTO = tasksService.addFile(id, fileModel, fileData);
        setLinkToFile(fileDTO, id);
        return fileDTO;
    }

    @PostMapping(value = "/{taskid}/files/{fileid}",
            consumes = {"multipart/form-data"})
    public FileDTO changeFile(@PathVariable("taskid") Long id, @PathVariable("fileid") Long fileId, @RequestPart("fileDescribe") String jsonDescribe,
                              @RequestPart(name = "file", required = false) MultipartFile fileData) throws IOException {
        log.info("TaskController: changeFile, docid-{}, fileDescribe{}", id, jsonDescribe);
        if (fileId == 0) {
            throw new BadRequestException("id файла не должен быть пустым");
        }
        NewFileDTO fileDescribe = new ObjectMapper().readValue(jsonDescribe, NewFileDTO.class);
        FileModel fileModel;
        try {
            fileModel = fileMapper.toModel(fileDescribe, fileData);
        } catch (IOException e) {
            log.error("Ошибка получения данных файла\n{}\n{}", e.getMessage(), e.getCause());
            throw new ServiceError("Ошибка получения данных файла");
        }
        fileModel.setId(fileId);
        FileDTO fileDTO = tasksService.changeFile(id, fileModel, fileData);
        setLinkToFile(fileDTO, id);
        log.info("TaskController: changeFile - changed, docid-{}, fileDescribe - {}, fileId-{}", id, fileDTO.getId(), jsonDescribe);
        return fileDTO;
    }

    @GetMapping(value = {"/{taskid}/files/{fileid}/download", "/{taskid}/files/{fileid}/download/{filename}"},
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getFileDataForDownload(@PathVariable("taskid") Long id, @PathVariable(name = "filename", required = false) Optional<String> reqFilename,
                                                         @PathVariable(name = "fileid") Long fileid) {
        log.info("TaskController: getFileDataForDownload");
        FileDataDTO fileData = tasksService.getFileData(id, fileid);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = reqFilename.orElse(fileData.getName()) + "." + fileData.getFileType();
        httpHeaders.set("Content-Disposition", "attachment; filename=" + fileName);
        return ResponseEntity.ok().headers(httpHeaders).body(fileData.getFileData());
    }

    @GetMapping(value = "/{taskid}/files/{fileid}/data")
    @ResponseBody
    public ResponseEntity<byte[]> getFileData(@PathVariable("taskid") Long id, @PathVariable("fileid") Long fileid) {
        log.info("TaskController: getFileData");
        FileDataDTO fileData = tasksService.getFileData(id, fileid);
        MediaType mt = MediaType.valueOf(fileData.getContentType());
        return ResponseEntity.ok().contentType(mt).body(fileData.getFileData());
    }

    @GetMapping(value = "/{taskid}/files")
    public Set<FileDTO> getFiles(@PathVariable("taskid") Long id) {
        log.info("TaskController: getFileslist");
        return tasksService.getFilesDTOList(id, this::setLinkToFile);
    }

    @GetMapping(value = "/{taskid}/files/{fileid}")
    public FileDTO getFileDescribe(@PathVariable("taskid") Long id, @PathVariable("fileid") Long fileid) {
        log.info("TaskController: getFileDescribe, docid-{}, fileid{}", id, fileid);
        return tasksService.getFileDTODescribe(
                id, fileid, this::setLinkToFile);
    }

    @DeleteMapping(value = "/{taskid}/files/{fileid}")
    public void deleteFile(@PathVariable("taskid") Long id, @PathVariable("fileid") Long fileid) {
        log.info("TaskController: deleteFile, docid-{}, fileid{}", id, fileid);
        tasksService.deleteFile(id, fileid);
        log.info("DocumentController: deleteFile - deleted, docid-{}, fileid{}", id, fileid);
    }

    @DeleteMapping(value = "/{taskid}/files/clear")
    public void clearFiles(@PathVariable("taskid") Long id) {
        log.info("TaskController: clearFiles, docid-{}", id);
        tasksService.clearFiles(id);
        log.info("DocumentController: clearFiles - done, docid-{}", id);
    }


}
