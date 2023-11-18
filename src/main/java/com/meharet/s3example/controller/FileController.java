package com.meharet.s3example.controller;

import com.meharet.s3example.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }


    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file)
    {
        return ResponseEntity.ok(storageService.uploadFile(file));
    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable(name = "fileName") String fileName)
    {
        byte[] bytes = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> delete(@PathVariable(name = "fileName") String fileName)
    {
        return ResponseEntity.ok(storageService.deleteFile(fileName));
    }

}
