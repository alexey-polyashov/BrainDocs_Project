package com.braindocs.braindocs.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class UploadFilesController {

    @PostMapping
    public String uploadFile(@RequestParam MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            File path = new File("F:/java/practika/text.txt");
            copyInputStreamToFile(is,path);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return "done";
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}