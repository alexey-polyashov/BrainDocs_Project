package com.braindocs.braindocs.services.documents;

import com.braindocs.braindocs.models.documents.DocumentViewModel;
import com.braindocs.braindocs.repositories.documents.DocumentViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentViewService {
    //Logger logger = LoggerFactory.getLogger(DocumentViewService.class);
    private final DocumentViewRepository documentViewRepository;

    public List<DocumentViewModel> findAll() {
        return documentViewRepository.findAll();
    }

    public String addView(String nameView, MultipartFile file) {

        try (InputStream is = file.getInputStream()) {
            //File path = new File("F:/java/practika/text.txt");
            //copyInputStreamToFile(is,path);
            copyInputStreamToDB(nameView, is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return "200";
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

    private void copyInputStreamToDB(String name, InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        int read;
        char ch;

        while ((read = inputStream.read()) != -1) {
            ch = (char) read;
            sb.append(ch);
        }
        //log.info("---------------" + sb.toString());
        documentViewRepository.save(new DocumentViewModel(name, sb.toString()));

    }

    public String findById(Long id) {
        Optional<DocumentViewModel> optionalDocumentViewModel = documentViewRepository.findById(id);
        DocumentViewModel documentViewModel = optionalDocumentViewModel.get();
        return documentViewModel.getDocumentData();
    }

    public String deleteById(Long id) {
        documentViewRepository.deleteById(id);
        return "Deleted";
    }
}
