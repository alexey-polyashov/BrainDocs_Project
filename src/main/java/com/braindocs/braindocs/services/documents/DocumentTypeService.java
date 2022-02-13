package com.braindocs.braindocs.services.documents;

import com.braindocs.braindocs.models.documents.DocumentTypeModel;
import com.braindocs.braindocs.repositories.documents.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DocumentTypeService {
    //Logger logger = LoggerFactory.getLogger(DocumentViewService.class);
    private final DocumentTypeRepository documentTypeRepository;

    public List<DocumentTypeModel> findAll() {
        return documentTypeRepository.findAll();
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
        documentTypeRepository.save(new DocumentTypeModel());

    }

    public DocumentTypeModel findById(Long id) {
        Optional<DocumentTypeModel> optionalDocumentViewModel = documentTypeRepository.findById(id);
        DocumentTypeModel documentViewModel = optionalDocumentViewModel.get();
        return documentViewModel;
    }

    public String deleteById(Long id) {
        documentTypeRepository.deleteById(id);
        return "Deleted";
    }
}
