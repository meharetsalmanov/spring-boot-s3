package com.meharet.s3example.controller;

import com.meharet.s3example.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController( FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file)
    {
        return ResponseEntity.ok(fileStorageService.storeFile(file));
    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable(name = "fileName") String fileName)
    {
        Resource resource = fileStorageService.loadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "fileName") String fileName)
    {
        return ResponseEntity.ok(fileStorageService.deleteFile(fileName));
    }

}
