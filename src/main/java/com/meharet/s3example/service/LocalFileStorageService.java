package com.meharet.s3example.service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileStorageService implements FileStorageService{

    private String localStoragePath;

    public LocalFileStorageService(String path) {
        localStoragePath = path;
        try {
            Files.createDirectories(Paths.get(localStoragePath).toAbsolutePath().normalize());
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory for file storage", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {

        Path root = Paths.get(localStoragePath);
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = System.currentTimeMillis() + "." + extension;

        try{
            Files.createDirectories(root);
            Files.copy(file.getInputStream(), root.resolve(filename));
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return filename;
    }

    @Override
    public Resource loadFile(String filename) {
        Path filePath = Paths.get(localStoragePath).resolve(filename);
        try{
            byte[] bytes= Files.readAllBytes(filePath);
            return new ByteArrayResource(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public Boolean deleteFile(String filename) {
        Path filePath = Paths.get(localStoragePath).resolve(filename);
        try{
            Files.delete(filePath);
            return true;
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
