package com.braindocs.braindocs.services.mappers;

import com.braindocs.braindocs.DTO.documents.DocumentDTO;
import com.braindocs.braindocs.DTO.documents.DocumentTypeNameDTO;
import com.braindocs.braindocs.DTO.organization.OrganisationNameDTO;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.organisations.OrganisationModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.sql.Date;
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
    private final OptionService optionService;

    public DocumentDTO toDTO(DocumentModel docModel) {

        DocumentDTO dto = new DocumentDTO();
        dto.setId(docModel.getId());
        dto.setDocumentType(new DocumentTypeNameDTO(docModel.getDocumentType()));
        dto.setNumber(docModel.getNumber());
        DateFormat dateFormat = new SimpleDateFormat(optionService.getDateFormat());
        dto.setDocumentDate(dateFormat.format(docModel.getDocumentDate()));
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

        if(docDTO.getId() != null) {
            docModel.setId(docDTO.getId());
        }
        docModel.setDocumentType(documentTypeService.findById(docDTO.getDocumentType().getId()));
        docModel.setNumber(docDTO.getNumber());
        SimpleDateFormat dateFormat = new SimpleDateFormat(optionService.getDateFormat());
        docModel.setDocumentDate(new Date(dateFormat.parse(docDTO.getDocumentDate()).getTime()));
        docModel.setHeading(docDTO.getHeading());
        docModel.setContent(docDTO.getContent());
        OrganisationModel orgModel = organisationService.findById(docDTO.getOrganisation().getId());
        docModel.setOrganisation(orgModel);
        UserModel userModel = userService.findById(docDTO.getAuthor().getId());
        docModel.setAuthor(userModel);
        userModel = userService.findById(docDTO.getResponsible().getId());
        docModel.setResponsible(userModel);
        docModel.setMarked(docDTO.getMarked());

        //docModel.setFiles(docDTO.getFiles().stream().map(fileMapper::toModel).collect(Collectors.toSet()));

        return docModel;
    }


}
