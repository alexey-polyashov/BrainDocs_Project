package com.braindocs.braindocs.services;

import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.files.FilesModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class DocumentsService {

    public Long addDocument(DocumentModel document){
        return 0L;
    }

    public Long saveDocument(DocumentModel document){
        return 0L;
    }

    public DocumentModel getDocumentById(Long documentId){
        return null;
    }

    public Page<DocumentModel> getDocumentsByFields(MultiValueMap<String, String> filter, int pageNumber, int pageSize){
        return null;
    }

    public Page<DocumentModel> getDocuments(int pageNumber, int pageSize){
        return null;
    }

    public Long addFile(FilesModel file){
        return null;
    }

    public FilesModel getFile(Long fileId){
        return null;
    }

}
