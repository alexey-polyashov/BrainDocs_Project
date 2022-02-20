package com.braindocs.services;

import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.repositories.DocumentTypeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
