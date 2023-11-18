package com.meharet.s3example.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.meharet.s3example.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class S3FileStorageService implements FileStorageService{

    private AmazonS3 s3Client;
    private final String s3BucketName;

    public S3FileStorageService(String bucketName,String accessKey,String secretKey,String region) {
        s3BucketName = bucketName;
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region).build();
    }

    @Override
    public String storeFile(MultipartFile file) {
        File fileObj = FileUtils.convertMultiPartFileToFile(file);
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(s3BucketName, filename, fileObj));
        fileObj.delete();
        return filename;
    }

    @Override
    public Resource loadFile(String filename) {
        S3ObjectInputStream stream = s3Client.getObject(new GetObjectRequest(s3BucketName, filename)).getObjectContent();
        try{
            byte[] bytes= IOUtils.toByteArray(stream);
            return new ByteArrayResource(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean deleteFile(String filename) {
        s3Client.deleteObject(s3BucketName,filename);
        return true;
    }
}
