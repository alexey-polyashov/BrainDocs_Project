package com.braindocs.braindocs.services;

import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.braindocs.repositories.DocumentTypeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository docTypeRepo;

    public DocumentTypeModel findById(Long id){
        Optional<DocumentTypeModel> docTypeModel = docTypeRepo.findById(id);
        return docTypeModel.orElseThrow(()->new ResourceNotFoundException("Тип документа '" + id + "' не найден"));
    }

}
