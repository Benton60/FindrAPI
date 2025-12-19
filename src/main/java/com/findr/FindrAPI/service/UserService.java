package com.findr.FindrAPI.service;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.Point;
import javax.naming.AuthenticationException;
import java.util.Objects;
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

    public User updateUser(User user) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();
        System.out.println(authenticatedUser);
        if(Objects.equals(authenticatedUser.getUsername(), user.getUsername())) {
            return userRepository.save(user);
        }else{
            throw new AuthenticationException();
        }
    }

    public User updateUserLocation(Point location) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();
        authenticatedUser.setLocation(location);
        return userRepository.save(authenticatedUser);
    }

    public User findByUsername(String username) throws AuthenticationException {
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
