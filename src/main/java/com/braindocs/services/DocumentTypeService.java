package com.braindocs.services;

import com.braindocs.exceptions.AnyOtherException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.repositories.DocumentTypeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeModel findById(Long id){
        Optional<DocumentTypeModel> docTypeModel = documentTypeRepository.findById(id);
        return docTypeModel.orElseThrow(()->new ResourceNotFoundException("Тип документа '" + id + "' не найден"));
    }

    public List<DocumentTypeModel> findAll() {
        return documentTypeRepository.findAll();
    }

    public Long addType(DocumentTypeModel documentTypeModel) {
        DocumentTypeModel doc = documentTypeRepository.save(documentTypeModel);
        return doc.getId();
    }

    public void deleteById(Long id) {
        if(id==null){
            throw new AnyOtherException("Id типа документа не указан");
        }
        documentTypeRepository.deleteById(id);
    }

    public void markById(Long id) {
        DocumentTypeModel docType = documentTypeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Тип документа с id '" + id + "' не найден"));
        docType.setMarked(true);
        documentTypeRepository.save(docType);
    }

}
