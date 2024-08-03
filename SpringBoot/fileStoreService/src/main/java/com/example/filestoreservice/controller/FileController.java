package com.example.filestoreservice.controller;


import com.example.filestoreservice.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        String fileId = fileStorageService.storeFile(file);
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws IOException {
        GridFsResource resource = fileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource.getInputStream().readAllBytes());
    }

    @GetMapping("/preview/{fileId}")
    public ResponseEntity<byte[]> previewFile(@PathVariable String fileId) throws IOException {
        GridFsResource resource = fileStorageService.getFile(fileId);

        // 处理图片和视频的预览
        MediaType mediaType = MediaType.parseMediaType(resource.getContentType());
        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM; // 默认类型
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource.getInputStream().readAllBytes());
    }
}
