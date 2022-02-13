package com.braindocs.braindocs.services.mappers;

import com.braindocs.braindocs.DTO.documents.DocumentTypeDTO;
import com.braindocs.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.braindocs.services.documents.DocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentTypeMapper {

    public DocumentTypeDTO toDTO(DocumentTypeModel docType){
        DocumentTypeDTO docTypeDTO = new DocumentTypeDTO();
        docTypeDTO.setMarked(docType.getMarked());
        docTypeDTO.setName(docType.getName());
        docTypeDTO.setId(docType.getId());
        return docTypeDTO;
    }

    public DocumentTypeModel toModel(DocumentTypeDTO docTypeDTO){
        DocumentTypeModel docType = new DocumentTypeModel();
        docType.setMarked(docTypeDTO.getMarked());
        docType.setName(docTypeDTO.getName());
        docType.setId(docTypeDTO.getId());
        return docType;
    }

}
