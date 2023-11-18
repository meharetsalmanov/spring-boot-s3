package com.meharet.s3example.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    Resource loadFile(String filename);

    Boolean deleteFile(String filename);
}
