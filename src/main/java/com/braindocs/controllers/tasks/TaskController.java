package com.braindocs.controllers.tasks;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Utils;
import com.braindocs.dto.FieldsListDTO;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.SearchCriteriaListDTO;
import com.braindocs.dto.documents.DocumentDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.dto.files.NewFileDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ServiceError;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.services.mappers.FileMapper;
import com.braindocs.services.mappers.TaskMapper;
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
import java.text.ParseException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/tasks")
@Slf4j
@Api(value = "TaskController", tags = "Контролер задач")
public class TaskController {

    private final TasksService tasksService;
    private final TaskMapper documentMapper;
    private final FileMapper fileMapper;

    private static final String STRING_TYPE = "String";
    private static final String LONG_TYPE = "Long";
    private static final String DATE_TYPE = "Date";

    private void setLinkToFile(FileDTO fileDTO, Long taskId){
        fileDTO.setLink("/api/v1/tasks/" + taskId + "/files/" + fileDTO.getId() + "/data");
    }

    @GetMapping(value="/fields")
    //возвращает список доступных полей и операций с ними
    //операции: ">" (больше или равно), "<" (меньше или равно), ":" (для строковых полей модели - содержит, для других = )
    //типы полей могут быть любыми - это просто описание типа для правильного построения интерфейса
    public Set<FieldsListDTO> getFields(){
        log.info("DocumentController: getFields");
        Set<FieldsListDTO> fieldsSet = new HashSet<>();
        fieldsSet.add(new FieldsListDTO("Вид задачи", "type", "/api/v1/tasks/types", Arrays.asList(":"), LONG_TYPE, false));
        log.info("DocumentController: getFields return {} elements", fieldsSet.size());
        return fieldsSet;
    }


}
