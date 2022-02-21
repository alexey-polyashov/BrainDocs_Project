package com.braindocs.services.mappers;

import com.braindocs.dto.documents.DocumentTypeDTO;
import com.braindocs.dto.documents.NewDocumentTypeDTO;
import com.braindocs.models.documents.DocumentTypeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentTypeMapper {

    private final FileMapper fileMapper;

    public DocumentTypeDTO toDTO(DocumentTypeModel docTypeModel){
        DocumentTypeDTO dto = new DocumentTypeDTO();
        dto.setId(docTypeModel.getId());
        dto.setName(docTypeModel.getName());
        dto.setMarked(docTypeModel.getMarked());

        dto.setFiles(docTypeModel.getFiles().stream().map(fileMapper::toDTO).collect(Collectors.toList()));

        return dto;
    }

    public DocumentTypeModel toModel(DocumentTypeDTO docTypeDTO){
        DocumentTypeModel docTypeModel = new DocumentTypeModel();
        docTypeModel.setId(docTypeDTO.getId());
        docTypeModel.setName(docTypeDTO.getName());
        docTypeModel.setMarked(docTypeDTO.getMarked());
        return docTypeModel;
    }

    public DocumentTypeModel toModel(NewDocumentTypeDTO docTypeDTO){
        DocumentTypeModel docTypeModel = new DocumentTypeModel();
        docTypeModel.setName(docTypeDTO.getName());
        docTypeModel.setMarked(docTypeDTO.getMarked());
        return docTypeModel;
    }

}
