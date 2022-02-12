package com.braindocs.braindocs.services;

import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.files.FileModel;
import com.braindocs.braindocs.repositories.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentsService {

    private final DocumentsRepository documentsRepository;
    private final FilesService filesService;

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

    public DocumentModel getDocumentById(Long documentId){
        return documentsRepository.findById(documentId).orElseThrow(()->new ResourceNotFoundException("Документ с id '" + documentId + "' не найден"));
    }

    public Page<DocumentModel> getDocumentsByFields(int page, int recordsOnPage, Specification spec){
        return documentsRepository.findAll(spec, PageRequest.of(page, recordsOnPage));
    }

    public Page<DocumentModel> getDocuments(int pageNumber, int pageSize){
        return documentsRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    //добавление файлов в документ списком
    @Transactional
    public Set<Long> addFiles(Long docId, Set<FileModel> files){
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
        Set<Long> results = new HashSet<>();
        for (FileModel file: files) {
            if(file.getId()!=0){
                log.error("file id is not empty");
                throw new RuntimeException("file id is not empty");
            }
            FileModel fileModel = filesService.add(file);
            documentModel.getFiles().add(fileModel);
            documentsRepository.save(documentModel);
            results.add(fileModel.getId());
        }
        return results;
    }

    //добавление одного файла
    @Transactional
    public Long addFile(Long docId, FileModel file){
        if(file.getId()!=0){
            log.error("file id is not empty");
            throw new RuntimeException("file id is not empty");
        }
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
        FileModel fileModel = filesService.add(file);
        documentModel.getFiles().add(fileModel);
        documentsRepository.save(documentModel);
        return fileModel.getId();
    }

    //добавление одного файла
    public Long changeFile(Long docId, FileModel file){
        if(file.getId()==0){
            log.error("file id is empty");
            throw new RuntimeException("file id is empty");
        }
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
        FileModel fileModel = filesService.findById(file.getId());
        if(fileModel.getFileData().length==0) {
            filesService.saveOnlyDescribtion(fileModel);
        }else{
            filesService.saveWithAllData(fileModel);
        }
        return fileModel.getId();
    }

    //получение данных файла по id
    public FileModel getFileData(Long docId, Long fileId){
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
        if(documentModel
                .getFiles()
                .stream()
                .anyMatch(p->p.getId()==fileId) == false){
            throw new ResourceNotFoundException("Файл с id-'" + fileId + "' не принадлежит документу с id-'" + docId + "'");
        }
        return filesService.findById(fileId);
    }

    //удаление файла по id
    @Transactional
    public void deleteFile(Long docId, Long fileId){
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
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
        filesService.delete(fileId);
        documentModel.getFiles().remove(fileModel);
        documentsRepository.save(documentModel);
    }

    //удаление всех файлов
    @Transactional
    public void clearFiles(Long docId){
        DocumentModel documentModel = documentsRepository.findById(docId)
                .orElseThrow(()->new ResourceNotFoundException("Документ с id-'" + docId + "' не найден"));
        for (FileModel file: documentModel.getFiles()) {
            filesService.delete(file.getId());
        }
        documentModel.getFiles().clear();
        documentsRepository.save(documentModel);
    }

}
