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

    public Optional<File> getFile(String subdirectory, String filename) {
        try {
            // Securely build the file path
            Path filePath = Paths.get(STORAGE_DIR, subdirectory, filename).normalize();
            File file = filePath.toFile();

            return (file.exists() && file.isFile()) ? Optional.of(file) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty(); // Fail safely
        }
    }
    public Optional<File> getFile(String filename) {
        try {
            // Securely build the file path
            Path filePath = Paths.get(filename).normalize();
            File file = filePath.toFile();

            return (file.exists() && file.isFile()) ? Optional.of(file) : Optional.empty();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty(); // Fail safely
        }
    }
}
