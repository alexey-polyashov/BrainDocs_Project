package com.braindocs.braindocs.services;

import com.braindocs.braindocs.common.Options;
import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.models.files.FileModel;
import com.braindocs.braindocs.repositories.FilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final FilesRepository filesRepository;
    private final Options options;

    public FileModel add(FileModel file){
        int storageType= options.getFileStorageType();
        if(storageType==1) {
            FileModel savedFile = filesRepository.save(file);
            return savedFile;
        }
        else{
            //1.сохранить сначала на диск
            //2.получить путь
            //3.очистить в модели данные файла
            //4.записать в базу модель
            return null;
        }
    }

    public Long saveWithAllData(FileModel file){
        int storageType= options.getFileStorageType();
        if(storageType==1) {
            Optional<FileModel> oldFile = filesRepository.findById(file.getId());
            oldFile.orElseThrow(() -> new ResourceNotFoundException("Файл с id - '" + file.getId() + "' не найден"));
            filesRepository.save(file);
            return file.getId();
        }
        else{
            //1.найти файл на диске
            //2.перезаписать файл на диске
            //3.получить путь
            //4.очистить в модели данные файла
            //5.записать в базу модель
            return 0L;
        }
    }

    public Long saveOnlyDescribtion(FileModel file){
        Optional<FileModel> oldFile = filesRepository.findById(file.getId());
        FileModel fileModel = oldFile.orElseThrow(()->new ResourceNotFoundException("Файл с id - '" + file.getId() + "' не найден"));
        fileModel.setDescribtion(file.getDescribtion());
        fileModel.setAuthor(file.getAuthor());
        fileModel.setName(file.getName());
        filesRepository.save(fileModel);
        return file.getId();
    }

    public FileModel findById(Long id){
        Optional<FileModel> file = filesRepository.findById(id);
        return file.orElseThrow(()->new ResourceNotFoundException("Файл с id - '" + id + "' не найден"));
    }

    public void delete(Long fileId) {
        filesRepository.deleteById(fileId);
    }
}
