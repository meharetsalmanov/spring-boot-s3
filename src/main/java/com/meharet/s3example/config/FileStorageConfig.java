package com.meharet.s3example.config;

import com.meharet.s3example.service.FileStorageService;
import com.meharet.s3example.service.LocalFileStorageService;
import com.meharet.s3example.service.S3FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Value("${file.storage.type}")
    private String storageType;

    @Value("${file.storage.local.path}")
    private String localStoragePath;

    @Value("${file.storage.s3.bucket-name}")
    private String s3BucketName;

    @Value("${file.storage.s3.credentials.access-key}")
    private String accessKey;

    @Value("${file.storage.s3.credentials.secret-key}")
    private String secretKey;

    @Value("${file.storage.s3.region.static}")
    private String region;

    @Bean
    public FileStorageService fileStorageService() {
        if ("local".equalsIgnoreCase(storageType)) {
            return new LocalFileStorageService(localStoragePath);
        } else if ("s3".equalsIgnoreCase(storageType)) {
            return new S3FileStorageService(s3BucketName,accessKey,secretKey,region);
        } else {
            throw new IllegalArgumentException("Invalid file storage type: " + storageType);
        }
    }
}
