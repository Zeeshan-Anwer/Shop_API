package com.shop.auto;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;

@Service
public class GoogleDriveService {

    @Autowired
    private Drive driveService;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // Create file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(multipartFile.getOriginalFilename());

        // Create file content
        InputStreamContent mediaContent = new InputStreamContent(
                multipartFile.getContentType(), 
                multipartFile.getInputStream());

        // Upload the file
        com.google.api.services.drive.model.File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        return "https://drive.google.com/uc?id=" + file.getId();
    }
}
