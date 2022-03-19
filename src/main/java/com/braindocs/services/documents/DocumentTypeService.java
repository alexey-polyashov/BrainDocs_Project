package com.braindocs.services.documents;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.documents.DocumentTypeDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.repositories.documents.DocumentTypeRepository;
import com.braindocs.repositories.specifications.DocumentTypeSpecificationBuilder;
import com.braindocs.services.FilesService;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.mappers.DocumentTypeMapper;
import com.braindocs.services.mappers.FileMapper;
import com.braindocs.services.users.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;
    private final FilesService filesService;
    private final FileMapper fileMapper;
    private final DocumentTypeMapper documentTypeMapper;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final Options options;


    public DocumentTypeModel findById(Long id) {
        Optional<DocumentTypeModel> docTypeModel = documentTypeRepository.findById(id);
        return docTypeModel.orElseThrow(() -> new ResourceNotFoundException("Тип документа '" + id + "' не найден"));
    }

    @Transactional
    public DocumentTypeDTO findDTOById(Long typeid, BiConsumer<FileDTO, Long> setLink) {
        Optional<DocumentTypeModel> docTypeModel = documentTypeRepository.findById(typeid);
        DocumentTypeDTO typeDTO = documentTypeMapper.toDTO(docTypeModel
                .orElseThrow(
                        () -> new ResourceNotFoundException("Тип документа '" + typeid + "' не найден")));
        if (typeDTO.getFiles() != null) {
            typeDTO.getFiles()
                    .forEach(fileDTO -> setLink.accept(fileDTO, typeid));
        }
        return typeDTO;

    }

    public List<DocumentTypeModel> findAll(MarkedRequestValue marked) {
        switch (marked) {
            case OFF:
                return documentTypeRepository.findByMarked(false);
            case ONLY:
                return documentTypeRepository.findByMarked(true);
            default:
                return documentTypeRepository.findAll();
        }
    }

    @Transactional
    public List<DocumentTypeDTO> findAllDTO(MarkedRequestValue marked, BiConsumer<FileDTO, Long> setLink) {

        return findAll(marked).stream()
                .map(documentTypeMapper::toDTO)
                .peek(typeDTO -> {
                    if (typeDTO.getFiles() != null) {
                        typeDTO.getFiles()
                                .forEach(fileDTO -> setLink.accept(fileDTO, typeDTO.getId()));
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<DocumentTypeModel> getTypesByFields(int pageNumber, int pageSize, List<SearchCriteriaDTO> filter) {

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

        DocumentTypeSpecificationBuilder builder =
                new DocumentTypeSpecificationBuilder(
                        options);
        for (SearchCriteriaDTO creteriaDTO : filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<DocumentTypeModel> spec = builder.build();

        return documentTypeRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
    }

    @Transactional
    public Page<DocumentTypeDTO> getTypesDTOByFields(int page, int recordsOnPage, List<SearchCriteriaDTO> filter) {
        Page<DocumentTypeModel> documentTypes = getTypesByFields(page, recordsOnPage, filter);
        return documentTypes.map(documentTypeMapper::toDTO);
    }

    @Transactional
    public Long addType(DocumentTypeModel documentTypeModel) {
        DocumentTypeModel doc = documentTypeRepository.save(documentTypeModel);
        return doc.getId();
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new BadRequestException("Id типа документа не указан");
        }
        documentTypeRepository.deleteById(id);
    }

    public void setMark(Long id, Boolean mark) {
        DocumentTypeModel docType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тип документа с id '" + id + "' не найден"));
        docType.setMarked(mark);
        documentTypeRepository.save(docType);
    }

    @Transactional
    public Long changeType(Long id, DocumentTypeModel docType) {
        DocumentTypeModel oldDocType = documentTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Вид документа с id '" + id + "' не найден"));
        oldDocType.setName(docType.getName());
        documentTypeRepository.save(oldDocType);
        return oldDocType.getId();
    }

    @Transactional
    public Long changeDTOType(Long id, DocumentTypeModel docType) {
        DocumentTypeModel oldDocType = documentTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Вид документа с id '" + id + "' не найден"));
        oldDocType.setName(docType.getName());
        documentTypeRepository.save(oldDocType);
        return oldDocType.getId();
    }

    //получение файла по id
    @Transactional
    public FileModel getDocumentTypeFile(Long docId, Long fileId) {
        DocumentTypeModel docTypeModel = findById(docId);
        FileModel fileModel = null;
        for (FileModel file : docTypeModel.getFiles()) {
            if (file.getId().equals(fileId)) {
                fileModel = file;
                break;
            }
        }
        if (fileModel == null) {
            throw new ResourceNotFoundException("Файл с id-'" + fileId + "' не принадлежит документу с id-'" + docId + "'");
        }
        return fileModel;
    }

    //добавление одного файла
    @Transactional
    public FileDTO addFile(Long docId, FileModel file, MultipartFile fileData) throws IOException {
        if (file.getId() != 0) {
            log.error("file id is not empty");
            throw new BadRequestException("Не указан id файла");
        }
        DocumentTypeModel documentTypeModel = findById(docId);
        FileModel fileModel = filesService.add(file, fileData);
        documentTypeModel.getFiles().add(fileModel);
        documentTypeRepository.save(documentTypeModel);
        return fileMapper.toDTO(fileModel);
    }

    @Transactional
    public Set<FileModel> getFilesList(Long docId) {
        DocumentTypeModel documentTypeModel = findById(docId);
        return documentTypeModel.getFiles();
    }

    @Transactional
    public Set<FileDTO> getFilesDTOList(Long docId, BiConsumer<FileDTO, Long> setLink) {
        DocumentTypeModel documentTypeModel = findById(docId);
        return documentTypeModel.getFiles().stream().map(
                p -> {
                    FileDTO res = fileMapper.toDTO(p);
                    setLink.accept(res, docId);
                    return res;
                }
        ).collect(Collectors.toSet());
    }

    //получение описания файла по id
    @Transactional
    public FileModel getFileDescribe(Long docId, Long fileId) {
        getDocumentTypeFile(docId, fileId);
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
    public FileDataDTO getFileData(Long docId, Long fileId) {
        getDocumentTypeFile(docId, fileId);
        return fileMapper.toDTOwithData(
                filesService.getFileData(fileId)
        );
    }

    //добавление одного файла
    @Transactional
    public FileDTO changeFile(Long docId, FileModel file, MultipartFile fileData) throws IOException {
        if (file.getId() == null || file.getId() == 0) {
            log.error("file 'id' is empty");
            throw new BadRequestException("Не определен 'id' изменяемого файла");
        }
        getDocumentTypeFile(docId, file.getId()); //проверка существования файла
        FileModel fileModel = null;
        if (fileData == null) {
            fileModel = filesService.saveOnlyDescribe(file);
        } else {
            fileModel = filesService.saveWithAllData(file, fileData);
        }
        return fileMapper.toDTO(fileModel);
    }

    //удаление файла по id
    @Transactional
    public void deleteFile(Long docId, Long fileId) {
        DocumentTypeModel documentTypeModel = findById(docId);
        FileModel fileModel = getDocumentTypeFile(docId, fileId);
        filesService.delete(fileId);
        documentTypeModel.getFiles().remove(fileModel);
        documentTypeRepository.save(documentTypeModel);
    }

    //удаление всех файлов
    @Transactional
    public void clearFiles(Long docId) {
        DocumentTypeModel documentTypeModel = findById(docId);
        for (FileModel file : documentTypeModel.getFiles()) {
            filesService.delete(file.getId());
        }
        documentTypeModel.getFiles().clear();
        documentTypeRepository.save(documentTypeModel);
    }

    @Transactional
    public Page<DocumentTypeDTO> getDocumentsDTO(int pageNumber, int pageSize) {
        Page<DocumentTypeModel> types = documentTypeRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return types.map(documentTypeMapper::toDTO);
    }

}
