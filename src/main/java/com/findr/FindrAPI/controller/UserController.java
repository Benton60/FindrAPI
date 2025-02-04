package com.findr.FindrAPI.controller;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(userService.encodePassword(user.getPassword())); // Hash the password
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/byUsername/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/byID/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findByID(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    // Add more user-related endpoints as needed
}
