package com.braindocs.braindocs.services.mappers;

import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.DTO.files.FileDTOwithData;
import com.braindocs.braindocs.DTO.files.NewFileDTOwithData;
import com.braindocs.braindocs.DTO.users.UserNameDTO;
import com.braindocs.braindocs.models.files.FileModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.FilesService;
import com.braindocs.braindocs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileMapper {

    private final UserService userService;
    private final FilesService filesService;

    public FileModel toModel(FileDTO fileDTO){
        FileModel file = new FileModel();
        file.setName(fileDTO.getName());
        file.setStorageType(fileDTO.getStorageType());
        file.setDescribtion(fileDTO.getDescribtion());
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
        dto.setDescribtion(fileModel.getDescribtion());
        dto.setFileSize(fileModel.getFileSize());
        dto.setFileType(fileModel.getFileType());
        dto.setAuthor(new UserNameDTO(fileModel.getAuthor()));
        dto.setOriginalFilename(fileModel.getOriginalFilename());
        dto.setContentType(fileModel.getContentType());
        return dto;
    }

    public FileDTOwithData toDTOwithData(FileModel fileModel){
        FileDTOwithData dto = new FileDTOwithData();
        dto.setId(fileModel.getId());
        dto.setName(fileModel.getName());
        dto.setStorageType(fileModel.getStorageType());
        dto.setDescribtion(fileModel.getDescribtion());
        dto.setFileType(fileModel.getFileType());
        dto.setAuthor(new UserNameDTO(fileModel.getAuthor()));
        dto.setParsedText(fileModel.getParsedText());
        dto.setOriginalFilename(fileModel.getOriginalFilename());
        dto.setContentType(fileModel.getContentType());
        return dto;
    }

    public FileModel toModel(NewFileDTOwithData fileData) throws IOException {
        FileModel file = new FileModel();
        file.setName(fileData.getName());
        file.setDescribtion(fileData.getDescribtion());
        file.setFileType(fileData.getFileType());
        UserModel userModel = userService.findById(fileData.getAuthor().getId());
        file.setAuthor(userModel);
        MultipartFile mpf = fileData.getFileData();
        file.setFileSize(mpf.getSize());
        file.setFileData(mpf.getBytes());
        file.setContentType(mpf.getContentType());
        file.setOriginalFilename(fileData.getOriginalFilename());
        return file;
    }

}
