package com.braindocs.braindocs.services.mappers;

import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import com.braindocs.braindocs.models.files.FilesModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMapper {

    private final UserService userService;

    public FilesModel toModel(FileDTO fileDTO){
        FilesModel filesModel = new FilesModel();
        filesModel.setId(fileDTO.getId());
        filesModel.setName(fileDTO.getName());
        filesModel.setStorageType(fileDTO.getStorageType());
        filesModel.setDescribtion(fileDTO.getDescribtion());
        filesModel.setFileSize(fileDTO.getFilesize());
        filesModel.setFileType(fileDTO.getFiletype());
        UserModel userModel = userService.findById(fileDTO.getAuthor().getId());
        filesModel.setAuthor(userModel);
        filesModel.setContent(fileDTO.getContent());
        filesModel.setSourcePath(fileDTO.getSourcePath());
        return filesModel;
    }

    public FileDTO toDTO(FilesModel filesModel){
        FileDTO dto = new FileDTO();
        dto.setId(filesModel.getId());
        dto.setName(filesModel.getName());
        dto.setStorageType(filesModel.getStorageType());
        dto.setDescribtion(filesModel.getDescribtion());
        dto.setFilesize(filesModel.getFileSize());
        dto.setFiletype(filesModel.getFileType());
        dto.setAuthor(new UserNameDTO(filesModel.getAuthor()));
        dto.setContent(filesModel.getContent());
        dto.setSourcePath(filesModel.getSourcePath());
        return dto;
    }
}
