package com.braindocs.braindocs.services;

import com.braindocs.braindocs.DTO.files.FileDTO;
import com.braindocs.braindocs.models.files.FilesModel;
import com.braindocs.braindocs.repositories.FilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final FilesRepository filesRepository;

    public Long save(FilesModel file){
        FilesModel savedFile = filesRepository.save(file);
        return savedFile.getId();
    }

}
