package com.braindocs.services;

import com.braindocs.DTO.files.FileDataDTO;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.repositories.DocumentsRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentsService {

    private final DocumentsRepository documentsRepository;
    private final FilesService filesService;
    private final FileMapper fileMapper;


    //получение документа по id
    private DocumentModel getDocument(Long docId){
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
        return documentModel;
    }

    //получение файла по id
    private FileModel getDocumentFile(Long docId, Long fileId){
        DocumentModel documentModel = getDocument(docId);
        FileModel fileModel = null;
        for (FileModel file: documentModel.getFiles()) {
            if(file.getId()==fileId){
                fileModel = file;
                break;
            }
        }
        if(fileModel==null){
            throw new ResourceNotFoundException("Файл с id-'" + fileId + "' не принадлежит документу с id-'" + docId + "'");
        }
        return fileModel;
    }

    //добавление документа без файлов
    public Long addDocument(DocumentModel document){
        DocumentModel doc = documentsRepository.save(document);
        return doc.getId();
    }

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
    public Page<DocumentModel> getDocumentsByFields(int page, int recordsOnPage, Specification spec){
        return documentsRepository.findAll(spec, PageRequest.of(page, recordsOnPage));
    }

    @Transactional
    public Page<DocumentModel> getDocuments(int pageNumber, int pageSize){
        return documentsRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    //добавление одного файла
    @Transactional
    public FileModel addFile(Long docId, FileModel file, MultipartFile fileData) throws IOException {
        if(file.getId()!=0){
            log.error("file id is not empty");
            throw new RuntimeException("file id is not empty");
        }
        DocumentModel documentModel = getDocument(docId);
        FileModel fileModel = filesService.add(file, fileData);
        documentModel.getFiles().add(fileModel);
        documentsRepository.save(documentModel);
        return fileModel;
    }

    @Transactional
    public Set<FileModel> getFilesList(Long docId){
        DocumentModel documentModel = getDocument(docId);
        return documentModel.getFiles();
    }

    //получение описания файла по id
    @Transactional
    public FileModel getFileDescribe(Long docId, Long fileId){
        getDocumentFile(docId, fileId);
        return filesService.findById(fileId);
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
    public FileModel changeFile(Long docId, FileModel file, MultipartFile fileData){
        if(file.getId()==null || file.getId()==0){
            log.error("file id is empty");
            throw new RuntimeException("file id is empty");
        }
        getDocumentFile(docId, file.getId());
        FileModel fileModel = filesService.findById(file.getId());
        if(fileData==null) {
            filesService.saveOnlyDescribe(fileModel);
        }else{
            filesService.saveWithAllData(fileModel);
        }
        return fileModel;
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
