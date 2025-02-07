package com.findr.FindrAPI.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

@Service
public class FileStorageService {
    private static final String STORAGE_DIR = "uploads";

    public FileStorageService() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    // Save uploaded file
    public String saveFile(MultipartFile file) {
        try {
            Path filePath = Paths.get(STORAGE_DIR, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    // Load file for downloading
    public Optional<File> getFile(String filename) {
        File file = new File(STORAGE_DIR, filename);
        return file.exists() ? Optional.of(file) : Optional.empty();
    }
}
