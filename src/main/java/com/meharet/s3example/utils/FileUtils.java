package com.meharet.s3example.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static File convertMultiPartFileToFile(MultipartFile file)
    {
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)){
            fileOutputStream.write(file.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
        return convertedFile;
    }
}
