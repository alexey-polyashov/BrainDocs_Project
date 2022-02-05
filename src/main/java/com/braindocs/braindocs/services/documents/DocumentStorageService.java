package com.braindocs.braindocs.services.documents;

import com.braindocs.braindocs.models.documents.DocumentStorageModel;
import com.braindocs.braindocs.repositories.documents.DocumentStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentStorageService {
    private final DocumentStorageRepository documentStorageRepository;

    public List<DocumentStorageModel> findAll() {
        return documentStorageRepository.findAll();
    }
}
