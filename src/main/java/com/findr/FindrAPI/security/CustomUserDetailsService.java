package com.findr.FindrAPI.security;

import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    //this function defines how a users credentials should be pulled from the database for the authentication check
    //because the fields don't necessarily match up perfectly
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Log the login attempt
        System.out.println("Login attempt with username: " + username);

        // Find the user in the repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    // Log the failed login attempt
                    System.out.println("Login failed for username: " + username + " (User not found)");
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        // Log the successful login attempt
        System.out.println("Login successful for username: " + username);

        // Return user details for Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // Set a default role for all users
                .build();
    }
}
