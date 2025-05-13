package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new APIException("Image name is empty");
        }

        String originalFilename = image.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new APIException("Image name is empty");
        }
        // Generate unique file name
        String randomUUID = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = randomUUID.concat(fileExtension);
        String filePath = path + File.separator + filename;

        // Check if folder exists
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdir()) throw new APIException("Unable to create folder");
        }

        // TODO: check if image for this user exists, delete if needed

        // Upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return filePath;
    }
}
