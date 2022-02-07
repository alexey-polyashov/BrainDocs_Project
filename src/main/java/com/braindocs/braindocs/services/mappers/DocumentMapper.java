package com.braindocs.braindocs.services.mappers;

import com.braindocs.braindocs.DTO.documents.DocumentDTO;
import com.braindocs.braindocs.DTO.documents.DocumentTypeNameDTO;
import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.organization.OrganisationNameDTO;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.organisations.OrganisationModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.DocumentTypeService;
import com.braindocs.braindocs.services.DocumentsService;
import com.braindocs.braindocs.services.OrganisationService;
import com.braindocs.braindocs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentMapper {

    private final DocumentsService documentsService;
    private final DocumentTypeMapper documentTypeMapper;
    private final DocumentTypeService documentTypeService;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final FileMapper fileMapper;

    public DocumentDTO toDTO(DocumentModel docModel) {

        DocumentDTO dto = new DocumentDTO();
        dto.setId(docModel.getId());
        dto.setDocumentType(new DocumentTypeNameDTO(docModel.getDocumentType()));
        dto.setNumber(docModel.getNumber());
        dto.setDocumentDate(docModel.getDocumentDate());
        dto.setHeading(docModel.getHeading());
        dto.setContent(docModel.getContent());
        dto.setAuthor(new UserNameDTO(docModel.getAuthor()));
        dto.setResponsible(new UserNameDTO(docModel.getResponsible()));
        dto.setOrganisation(new OrganisationNameDTO(docModel.getOrganisation()));
        dto.setMarked(docModel.getMarked());

        dto.setFiles(docModel.getFiles().stream().map(fileMapper::toDTO).collect(Collectors.toList()));

        return dto;

    }

    public DocumentModel toModel(DocumentDTO docDTO) {

        DocumentModel docModel = new DocumentModel();

        docModel.setId(docDTO.getId());
        docModel.setDocumentType(documentTypeService.findById(docDTO.getId()));
        docModel.setNumber(docDTO.getNumber());
        docModel.setDocumentDate(docDTO.getDocumentDate());
        docModel.setHeading(docDTO.getHeading());
        docModel.setContent(docDTO.getContent());
        OrganisationModel orgModel = organisationService.findById(docModel.getOrganisation().getId());
        docModel.setOrganisation(orgModel);
        UserModel userModel = userService.findById(docModel.getAuthor().getId());
        docModel.setAuthor(userModel);
        userModel = userService.findById(docModel.getResponsible().getId());
        docModel.setResponsible(userModel);
        docModel.setMarked(docDTO.getMarked());

        docModel.setFiles(docDTO.getFiles().stream().map(fileMapper::toModel).collect(Collectors.toSet()));
        docModel.setDocumentType(documentTypeService.findById(docDTO.getId()));

        return docModel;
    }


}
