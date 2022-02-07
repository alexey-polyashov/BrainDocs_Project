package com.braindocs.braindocs.services;

import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.files.FilesModel;
import com.braindocs.braindocs.repositories.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    public Long addFiles(Long docId, Set<FilesModel> files){
        return null;
    }

    //получение списка файлов
    public FilesModel getFilesList(Long docId, Long fileId){
        return null;
    }

    //добавление одного файла
    public Long addFile(Long docId, FilesModel file){
        return null;
    }

    //получение данных файла по id
    public FilesModel getFile(Long docId, Long fileId){
        return null;
    }

}
