package com.braindocs.services.documents;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.dto.documents.DocumentDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.repositories.documents.DocumentsRepository;
import com.braindocs.repositories.specifications.DocumentSpecificationBuilder;
import com.braindocs.services.FilesService;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.mappers.DocumentMapper;
import com.braindocs.services.mappers.FileMapper;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentsService {

    private final DocumentsRepository documentsRepository;
    private final FilesService filesService;
    private final FileMapper fileMapper;
    private final DocumentMapper documentMapper;
    private final DocumentTypeService documentTypeService;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final Options options;

    //получение документа по id
    public DocumentModel getDocument(Long docId){
        return documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
    }

    //добавление документа без файлов
    public Long addDocument(DocumentModel document){
        document.setMarked(false);
        DocumentModel doc = documentsRepository.save(document);
        return doc.getId();
    }

    @Transactional
    public Long saveDocument(DocumentModel document){
        DocumentModel oldDoc = getDocumentById(document.getId());
        //чтение данных для добавления файлов в модель, которые уже загружены
        //при изменении документа в DTO нет списка файлов
        document.setFiles(
                oldDoc.getFiles().stream()
                        .filter(Objects::nonNull)
                        .peek(p->document.getFiles().add(p))
                        .collect(Collectors.toSet())
        );
        document.setMarked(oldDoc.getMarked());
        DocumentModel doc = documentsRepository.save(document);
        return doc.getId();
    }

    public void deleteDocument(Long id){
        documentsRepository.deleteById(id);
    }

    @Transactional
    public DocumentModel getDocumentById(Long documentId){
        return documentsRepository.findById(documentId).orElseThrow(()->new ResourceNotFoundException("Документ с id '" + documentId + "' не найден"));
    }

    @Transactional
    public DocumentDTO getDocumentDTOById(Long documentId){
        return documentMapper.toDTO(
                documentsRepository
                        .findById(documentId)
                        .orElseThrow(()->new ResourceNotFoundException("Документ с id '" + documentId + "' не найден"))
        );
    }

    @Transactional
    public Page<DocumentModel> getDocumentsByFields(int pageNumber, int pageSize, List<SearchCriteriaDTO> filter){

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

        DocumentSpecificationBuilder builder = new DocumentSpecificationBuilder(userService, organisationService, documentTypeService, options);
        for(SearchCriteriaDTO creteriaDTO: filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<DocumentModel> spec = builder.build();

        return documentsRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

    }

    @Transactional
    public Page<DocumentDTO> getDocumentsDTOByFields(int page, int recordsOnPage, List<SearchCriteriaDTO> filter){
        Page<DocumentModel> documents = getDocumentsByFields(page, recordsOnPage, filter);
        return documents.map(documentMapper::toDTO);
    }

    @Transactional
    public Page<DocumentModel> getDocuments(int pageNumber, int pageSize, MarkedRequestValue marked){
        switch(marked){
            case ON:
                return documentsRepository.findByMarked(false, PageRequest.of(pageNumber, pageSize));
            case ONLY:
                return documentsRepository.findByMarked(true, PageRequest.of(pageNumber, pageSize));
            default:
                return documentsRepository.findAll(PageRequest.of(pageNumber, pageSize));
        }
    }

    @Transactional
    public Page<DocumentDTO> getDocumentsDTO(int pageNumber, int pageSize, MarkedRequestValue marked, BiConsumer<FileDTO, Long> setLink) {
        Page<DocumentModel> documents = getDocuments(pageNumber, pageSize, marked);
        return documents.map(
                docModel->{
                    DocumentDTO docDto = documentMapper.toDTO(docModel);
                    docDto.getFiles()
                            .stream().forEach(p->setLink.accept(p, p.getId()));
                    return docDto;
                });
    }

    public void setMark(Long id, Boolean mark) {
        DocumentModel doc = documentsRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id '" + id + "' не найден"));
        doc.setMarked(mark);
        documentsRepository.save(doc);
    }

    //получение файла по id
    @Transactional
    public FileModel getDocumentFile(Long docId, Long fileId){
        DocumentModel documentModel = getDocument(docId);
        FileModel fileModel = null;
        for (FileModel file: documentModel.getFiles()) {
            if(file.getId().equals(fileId)){
                fileModel = file;
                break;
            }
        }
        if(fileModel==null){
            throw new ResourceNotFoundException("Файл с id-'" + fileId + "' не принадлежит документу с id-'" + docId + "'");
        }
        return fileModel;
    }

    //добавление одного файла
    @Transactional
    public FileDTO addFile(Long docId, FileModel file, MultipartFile fileData) throws IOException {
        if(file.getId()!=0){
            log.error("file id is not empty");
            throw new BadRequestException("Id файла должен быть пустым");
        }
        DocumentModel documentModel = getDocument(docId);
        FileModel fileModel = filesService.add(file, fileData);
        documentModel.getFiles().add(fileModel);
        documentsRepository.save(documentModel);
        return fileMapper.toDTO(fileModel);
    }

    @Transactional
    public Set<FileModel> getFilesList(Long docId){
        DocumentModel documentModel = getDocument(docId);
        return documentModel.getFiles();
    }

    @Transactional
    public Set<FileDTO> getFilesDTOList(Long docId, BiConsumer<FileDTO, Long> setLink){
        DocumentModel documentModel = getDocument(docId);
        return documentModel.getFiles().stream().map(
                p->{
                    FileDTO res = fileMapper.toDTO(p);
                    setLink.accept(res, docId);
                    return res;
                }
        ).collect(Collectors.toSet());
    }
    //получение описания файла по id
    @Transactional
    public FileModel getFileDescribe(Long docId, Long fileId){
        getDocumentFile(docId, fileId);
        return filesService.findById(fileId);
    }

    @Transactional
    public FileDTO getFileDTODescribe(Long docId, Long fileId, BiConsumer<FileDTO, Long> setLink){
        FileDTO fDTO = fileMapper.toDTO(filesService.findById(fileId));
        setLink.accept(fDTO, docId);
        return fDTO;
    }

    //получение данных файла по id
    @Transactional
    public FileDataDTO getFileData(Long docId, Long fileId){
        getDocumentFile(docId, fileId);
        return fileMapper.toDTOwithData(
                filesService.getFileData(fileId)
        );
    }

    //добавление одного файла
    @Transactional
    public FileDTO changeFile(Long docId, FileModel file, MultipartFile fileData) throws IOException {
        if(file.getId()==null || file.getId()==0){
            log.error("file 'id' is empty");
            throw new BadRequestException("Не определен 'id' изменяемого файла");
        }
        getDocumentFile(docId, file.getId()); //проверка существования файла
        FileModel fileModel=null;
        if(fileData==null || fileData.isEmpty()) {
            fileModel = filesService.saveOnlyDescribe(file);
        }else{
            fileModel = filesService.saveWithAllData(file, fileData);
        }
        return fileMapper.toDTO(fileModel);
    }

    //удаление файла по id
    @Transactional
    public void deleteFile(Long docId, Long fileId){
        DocumentModel documentModel = getDocument(docId);
        FileModel fileModel = getDocumentFile(docId, fileId);
        filesService.delete(fileId);
        documentModel.getFiles().remove(fileModel);
        documentsRepository.save(documentModel);
    }

    //удаление всех файлов
    @Transactional
    public void clearFiles(Long docId){
        DocumentModel documentModel = getDocument(docId);
        for (FileModel file: documentModel.getFiles()) {
            filesService.delete(file.getId());
        }
        documentModel.getFiles().clear();
        documentsRepository.save(documentModel);
    }

}
