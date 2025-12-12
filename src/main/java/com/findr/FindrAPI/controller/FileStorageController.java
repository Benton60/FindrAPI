package com.findr.FindrAPI.controller;




import com.findr.FindrAPI.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    //this was an endpoint, but I low-key don't think it'll be used.
//    // Upload a file
//    @PostMapping("/upload/post")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        String filePath = fileStorageService.saveFile(file);
//        return ResponseEntity.ok("File uploaded: " + filePath);
//    }

    //Upload a Profile Photo
    @PostMapping(value = "/upload/profile/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfile(@PathVariable String username, @RequestPart("image") MultipartFile file) {
        try{
            return new ResponseEntity<>(fileStorageService.saveProfilePic(username, file), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    // Download a file
    @GetMapping("/download/profile/{user}")
    public ResponseEntity<Resource> downloadProfilePhoto(@PathVariable String user) {
        System.out.println(user + "/" + "profile");
        Optional<File> fileOpt = fileStorageService.getProfileFile(user);
        try {
            return loadFileFromStorage(fileOpt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/post/{filePath}")
    public ResponseEntity<Resource> downloadPost(@PathVariable String filePath) {
        System.out.println(filePath);
        filePath = filePath.replace(" ", "\\");
        System.out.println(filePath);
        Optional<File> fileOpt = fileStorageService.getFile(filePath);

        try{
            return loadFileFromStorage(fileOpt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    private ResponseEntity<Resource> loadFileFromStorage(Optional<File> fileOpt) throws IOException {
        if (fileOpt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        File file = fileOpt.get();
            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(file.toPath())))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
    }
}