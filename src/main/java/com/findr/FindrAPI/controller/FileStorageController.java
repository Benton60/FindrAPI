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


    //Upload a Profile Photo
    @PostMapping(value = "/upload/profile/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePhoto(@PathVariable String username, @RequestPart("image") MultipartFile file) {
        try{
            return new ResponseEntity<>(fileStorageService.saveProfilePic(username, file), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    // The client uses this to retrieve profile pics
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

    // The client uses this to retrieve a specific posts photo
    @GetMapping("/download/post/{author}/{postID}/{fileName}")
    public ResponseEntity<Resource> downloadPostPhoto(@PathVariable String author, @PathVariable Long postID, @PathVariable String fileName) {
        System.out.println(fileName);
        Optional<File> fileOpt = fileStorageService.getFile(author , postID, fileName);

        try{
            return loadFileFromStorage(fileOpt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //this function doesn't really handle the file data as much as it converts it into HTTP response entity
    private ResponseEntity<Resource> loadFileFromStorage(Optional<File> fileOpt) throws IOException {
        if (fileOpt.isEmpty()) {
            //We just return a blank picture, its easier and faster than trying to return an http error
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