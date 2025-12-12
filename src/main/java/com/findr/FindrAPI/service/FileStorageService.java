package com.findr.FindrAPI.service;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileStorageService {
    @Autowired
    private UserRepository userRepository;

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
        // Ensure user is authenticated and matches the path user
        if (!username.equals(getAuthenticatedUser().getUsername())) {
            throw new AuthenticationException("Cannot make a profile for a different username");
        }

        try {
            // Build the user's directory
            Path userDir = Paths.get(STORAGE_DIR, username).normalize();
            Files.createDirectories(userDir); // Create directory if it does not exist

            // 1. Delete any existing profile.* file
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(userDir, "profile.*")) {
                for (Path oldFile : stream) {
                    Files.deleteIfExists(oldFile);
                }
            }

            // 2. Extract extension safely
            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new RuntimeException("File must have an extension");
            }

            String extension = originalName.substring(originalName.lastIndexOf('.') + 1);

            // 3. Create new path: profile.<ext>
            Path newFilePath = userDir.resolve("profile." + extension);

            // 4. Save file (replaceExisting for extra safety)
            Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

            return newFilePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to save profile picture", e);
        }
    }

    public Optional<File> getProfileFile(String username) {
        try {
            // Build directory path safely
            Path dirPath = Paths.get(STORAGE_DIR, username).normalize();
            File directory = dirPath.toFile();

            if (!directory.exists() || !directory.isDirectory()) {
                return Optional.empty();
            }

            // Look for a file named "profile" with ANY extension
            File[] matches = directory.listFiles((dir, name) -> {
                return name.startsWith("profile.");
            });

            if (matches != null && matches.length > 0) {
                return Optional.of(matches[0]); // return first match
            }

            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
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
