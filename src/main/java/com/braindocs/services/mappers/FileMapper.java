package com.braindocs.services.mappers;

import com.braindocs.dto.files.FileDTO;
import com.braindocs.dto.files.FileDataDTO;
import com.braindocs.dto.files.NewFileDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.common.Options;
import com.braindocs.models.files.FileModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.FilesService;
import com.braindocs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileMapper {

    private final UserService userService;
    private final FilesService filesService;
    private final Options options;

    public FileModel toModel(FileDTO fileDTO){
        FileModel file = new FileModel();
        file.setName(fileDTO.getName());
        file.setStorageType(fileDTO.getStorageType());
        file.setDescription(fileDTO.getDescription());
        file.setFileType(fileDTO.getFileType());
        UserModel userModel = userService.findById(fileDTO.getAuthor().getId());
        file.setAuthor(userModel);
        file.setOriginalFilename(fileDTO.getOriginalFilename());
        file.setFileSize(fileDTO.getFileSize());
        file.setContentType(fileDTO.getContentType());
        file.setOriginalFilename(fileDTO.getOriginalFilename());
        return file;
    }

    public FileDTO toDTO(FileModel fileModel){
        FileDTO dto = new FileDTO();
        dto.setId(fileModel.getId());
        dto.setName(fileModel.getName());
        dto.setStorageType(fileModel.getStorageType());
        dto.setDescription(fileModel.getDescription());
        dto.setFileSize(fileModel.getFileSize());
        dto.setFileType(fileModel.getFileType());
        dto.setAuthor(new UserNameDTO(fileModel.getAuthor()));
        dto.setOriginalFilename(fileModel.getOriginalFilename());
        dto.setContentType(fileModel.getContentType());
        return dto;
    }

    public FileDataDTO toDTOwithData(FileModel fileModel){
        FileDataDTO dto = new FileDataDTO();
        dto.setId(fileModel.getId());
        dto.setName(fileModel.getName());
        dto.setFileType(fileModel.getFileType());
        dto.setContentType(fileModel.getContentType());
        dto.setFileData(fileModel.getFileData());
        return dto;
    }

    public FileModel toModel(NewFileDTO fileData, MultipartFile mpf) throws IOException {
        FileModel file = new FileModel();
        file.setId(0L);
        file.setStorageType(options.getFileStorageType());
        file.setName(fileData.getName());
        file.setDescription(fileData.getDescription());
        file.setFileType(fileData.getFileType());
        UserModel userModel = userService.findById(fileData.getAuthor().getId());
        file.setAuthor(userModel);
        file.setFileSize(mpf.getSize());
        file.setFileData(mpf.getBytes());
        file.setContentType(mpf.getContentType());
        file.setOriginalFilename(fileData.getOriginalFilename());
        return file;
    }

}
