package com.meharet.s3example.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class StorageService {

    private final AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    public StorageService(AmazonS3 amazonS3) {
        this.s3Client = amazonS3;
    }

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultipartFileToFile(file);
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, filename, fileObj));
        fileObj.delete();
        return "File uploaded: " + filename;

    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "File deleted: " + fileName;
    }


    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return convertedFile;
    }
}
