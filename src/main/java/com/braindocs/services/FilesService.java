package com.braindocs.services;

import com.braindocs.common.Options;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.files.FileModel;
import com.braindocs.repositories.FilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final FilesRepository filesRepository;
    private final Options options;

    public FileModel add(FileModel file, MultipartFile fileData) throws IOException {
        int storageType= options.getFileStorageType();
        if(storageType==1) {
            file.setFileData(fileData.getBytes());
            return filesRepository.save(file);
        }
        else{
            //1.сохранить сначала на диск
            //2.получить путь
            //3.очистить в модели данные файла
            //4.записать в базу модель
            return null;
        }
    }

    public FileModel saveWithAllData(FileModel file, MultipartFile fileData) throws IOException {
        int storageType= options.getFileStorageType();
        if(storageType==1) {
            Optional<FileModel> optOldFile = filesRepository.findById(file.getId());
            FileModel oldFile = optOldFile.orElseThrow(() -> new ResourceNotFoundException("Файл с id - '" + file.getId() + "' не найден"));
            oldFile.setDescription(file.getDescription());
            oldFile.setAuthor(file.getAuthor());
            oldFile.setName(file.getName());
            oldFile.setFileData(fileData.getBytes());
            oldFile.setContentType(fileData.getContentType());
            filesRepository.save(oldFile);
            return file;
        }
        else{
            //1.найти файл на диске
            //2.перезаписать файл на диске
            //3.получить путь
            //4.очистить в модели данные файла
            //5.записать в базу модель
            return null;
        }
    }

    public FileModel saveOnlyDescribe(FileModel file){
        Optional<FileModel> oldFile = filesRepository.findById(file.getId());
        FileModel fileModel = oldFile.orElseThrow(()->new ResourceNotFoundException("Файл с id - '" + file.getId() + "' не найден"));
        fileModel.setDescription(file.getDescription());
        fileModel.setAuthor(file.getAuthor());
        fileModel.setName(file.getName());
        filesRepository.save(fileModel);
        return file;
    }

    public FileModel findById(Long id){
        Optional<FileModel> file = filesRepository.findById(id);
        return file.orElseThrow(()->new ResourceNotFoundException("Файл с id - '" + id + "' не найден"));
    }

    public FileModel getFileData(Long id){
        Optional<FileModel> file = filesRepository.findById(id);
        int storageType= options.getFileStorageType();
        if(storageType==2) {
            //если файлы хранятся на диске
            //1.найти файл на диске
            //2.прочитать файл в данные модели
            return null;
        }else{
            //если файлы хранятся в базе
            return file.orElseThrow(()->new ResourceNotFoundException("Файл с id - '" + id + "' не найден"));
        }
    }

    public void delete(Long fileId) {
        filesRepository.deleteById(fileId);
    }
}
