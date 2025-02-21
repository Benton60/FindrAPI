package com.findr.FindrAPI.controller;




import com.findr.FindrAPI.service.FileStorageService;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {
    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Upload a file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.saveFile(file);
        return ResponseEntity.ok("File uploaded: " + filePath);
    }

    // Download a file
    @GetMapping("/download/profile/{user}/{filename}")
    public ResponseEntity<Resource> downloadProfilePhoto(@PathVariable String user, @PathVariable String filename) {
        System.out.println(user + "/" + filename);
        Optional<File> fileOpt = fileStorageService.getFile(user, filename);
        if (fileOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        File file = fileOpt.get();
        try {
            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(file.toPath())))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}