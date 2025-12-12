package com.findr.FindrAPI.controller;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.service.UserService;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;


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
    @PostMapping("/updateLocation")
    public ResponseEntity<User> updateLocation(@RequestBody Point location) {
        try{
            User user = userService.updateUserLocation(location);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try{
            userService.updateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //this get mapping has no service function it serves only to check sign-in credentials
    @GetMapping("/checkCredentials")
    public ResponseEntity<User>checkCredentials(){
        return new ResponseEntity<>(new User(), HttpStatus.OK);
    }
    @GetMapping("/byUsername/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
