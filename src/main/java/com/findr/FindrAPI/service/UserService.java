package com.findr.FindrAPI.service;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        //this makes sure that the users password hash never makes it past the service layer
        if (userOptional.isPresent()) {
            userOptional.get().setPassword("");
        }
        return userOptional.orElse(null); // Return null if user not found
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User findByID(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userOptional.get().setPassword("");
        }
        return userOptional.orElse(null);
    }
}
