package com.braindocs.services.documents;

import com.braindocs.dto.documents.DocumentDTO;
import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.exceptions.AnyOtherException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.repositories.documents.DocumentsRepository;
import com.braindocs.services.FilesService;
import com.braindocs.services.mappers.DocumentMapper;
import com.braindocs.services.mappers.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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


    //получение документа по id
    private DocumentModel getDocument(Long docId){
        return documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
    }

    //добавление документа без файлов
    public Long addDocument(DocumentModel document){
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
                        .peek(p->document.getFiles().add(p))
                        .collect(Collectors.toSet())
        );
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
    public Page<DocumentModel> getDocumentsByFields(int page, int recordsOnPage, Specification spec){
        return documentsRepository.findAll(spec, PageRequest.of(page, recordsOnPage));
    }

    @Transactional
    public Page<DocumentDTO> getDocumentsDTOByFields(int page, int recordsOnPage, Specification spec){
        Page<DocumentModel> documents = getDocumentsByFields(page, recordsOnPage, spec);
        return documents.map(documentMapper::toDTO);
    }

    @Transactional
    public Page<DocumentModel> getDocuments(int pageNumber, int pageSize){
        return documentsRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Transactional
    public Page<DocumentDTO> getDocumentsDTO(int pageNumber, int pageSize, BiConsumer<FileDTO, Long> setLink) {
        Page<DocumentModel> documents = documentsRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return documents.map(
                docModel->{
                    DocumentDTO docDto = documentMapper.toDTO(docModel);
                    docDto.getFiles()
                            .stream().forEach(p->setLink.accept(p, p.getId()));
                    return docDto;
                });
    }

    public void markDocument(Long id) {
        DocumentModel doc = documentsRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id '" + id + "' не найден"));
        doc.setMarked(true);
        documentsRepository.save(doc);
    }

    //получение файла по id
    @Transactional
    private FileModel getDocumentFile(Long docId, Long fileId){
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
            throw new RuntimeException("file id is not empty");
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
            throw new AnyOtherException("Не определен 'id' изменяемого файла");
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
