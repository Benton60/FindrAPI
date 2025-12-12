package com.findr.FindrAPI.service;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.*;
import java.nio.file.*;
import java.util.Optional;

@Service
public class FileStorageService {

    private static UserRepository userRepository;
    private static final String STORAGE_DIR = "uploads";

    public FileStorageService() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    // Save uploaded file
    public String saveProfilePic(String username, MultipartFile file) throws AuthenticationException {
        if(username == getAuthenticatedUser().getUsername().toString()) {
            try {
                Path filePath = Paths.get(STORAGE_DIR + "/" + username, file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath.toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }
        }else{
            throw new AuthenticationException("Cannot Make a Profile For a Different Username");
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
    // Helper method to get the authenticated user
    private User getAuthenticatedUser() throws AuthenticationException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authenticatedUsername;

        if (principal instanceof UserDetails) {
            authenticatedUsername = ((UserDetails) principal).getUsername();
        } else {
            authenticatedUsername = principal.toString();
        }

        Optional<User> authenticatedUserOpt = userRepository.findByUsername(authenticatedUsername);
        if (authenticatedUserOpt.isEmpty()) {
            throw new AuthenticationException("Authenticated user not found.");
        }

        return authenticatedUserOpt.get();
    }
}
