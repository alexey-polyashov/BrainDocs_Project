package com.braindocs.services.mappers;

import com.braindocs.common.Options;
import com.braindocs.dto.documents.DocumentDTO;
import com.braindocs.dto.documents.DocumentTypeNameDTO;
import com.braindocs.dto.organization.OrganisationNameDTO;
import com.braindocs.dto.tasks.TaskSubjectDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.models.organisations.OrganisationModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.documents.DocumentTypeService;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentMapper {

    private final DocumentTypeService documentTypeService;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final FileMapper fileMapper;
    private final Options options;

    public DocumentDTO toDTO(DocumentModel docModel) {

        DocumentDTO dto = new DocumentDTO();
        dto.setId(docModel.getId());
        dto.setDocumentType(new DocumentTypeNameDTO(docModel.getDocumentType()));
        dto.setNumber(docModel.getNumber());
        dto.setDocumentDate(options.convertDateToString(docModel.getDocumentDate()));
        dto.setHeading(docModel.getHeading());
        dto.setContent(docModel.getContent());
        dto.setAuthor(new UserNameDTO(docModel.getAuthor()));
        dto.setResponsible(new UserNameDTO(docModel.getResponsible()));
        dto.setOrganisation(new OrganisationNameDTO(docModel.getOrganisation()));
        dto.setMarked(docModel.getMarked());

        dto.setFiles(docModel.getFiles().stream().map(fileMapper::toDTO).collect(Collectors.toList()));

        return dto;

    }

    public DocumentModel toModel(DocumentDTO docDTO) throws ParseException {

        DocumentModel docModel = new DocumentModel();

        if (docDTO.getId() != null) {
            docModel.setId(docDTO.getId());
        }
        docModel.setDocumentType(documentTypeService.findById(docDTO.getDocumentType().getId()));
        docModel.setNumber(docDTO.getNumber());
        docModel.setDocumentDate(options.convertStringToDate(docDTO.getDocumentDate()));
        docModel.setHeading(docDTO.getHeading());
        docModel.setContent(docDTO.getContent());
        OrganisationModel orgModel = organisationService.findById(docDTO.getOrganisation().getId());
        docModel.setOrganisation(orgModel);
        UserModel userModel = null;
        if (docDTO.getAuthor() != null) {
            userModel = userService.findById(docDTO.getAuthor().getId());
            docModel.setAuthor(userModel);
        }
        docModel.setFiles(new HashSet<FileModel>());
        if (docDTO.getResponsible() != null) {
            userModel = userService.findById(docDTO.getResponsible().getId());
            docModel.setResponsible(userModel);
        }

        return docModel;
    }

    public void moveChange(DocumentModel receiver, DocumentDTO sourceDTO) throws ParseException {
        DocumentModel source = this.toModel(sourceDTO);
        receiver.setHeading(source.getHeading());
        receiver.setContent(source.getContent());
        if (source.getAuthor() != null) {
            receiver.setAuthor(source.getAuthor());
        }
        if (source.getResponsible() != null) {
            receiver.setAuthor(source.getResponsible());
        }
        receiver.setOrganisation(source.getOrganisation());
    }

    public TaskSubjectDTO toSubjectDTO(DocumentModel docModel) {

        TaskSubjectDTO dto = new TaskSubjectDTO();
        dto.setId(docModel.getId());
        dto.setSubjectType(docModel.getDocumentType().getName());
        dto.setNumber(docModel.getNumber());
        dto.setDate(options.convertDateToString(docModel.getDocumentDate()));
        dto.setName(docModel.getHeading());

        return dto;
    }

}
