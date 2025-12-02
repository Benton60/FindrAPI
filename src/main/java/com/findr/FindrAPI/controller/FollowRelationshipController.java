package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.FollowRelationship;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.service.FollowRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FollowRelationshipController {
    @Autowired
    private FollowRelationshipService followRelationshipService;

    @PostMapping("/addFriend/{username}")
    public ResponseEntity<FollowRelationship> addFriend(@PathVariable String username){
        try {
            followRelationshipService.addFriend(username);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
    }
    @DeleteMapping("/removeFriend/{username}")
    public ResponseEntity<Boolean> removeFriend(@PathVariable String username){
        try {
            followRelationshipService.deleteFriend(username);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/friends/{username}")
    public ResponseEntity<List<User>> getFriends(@PathVariable String username){
        try {
            return new ResponseEntity<>(followRelationshipService.getFriends(username), HttpStatus.OK);
        }catch(IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/followers/{username}")
    public ResponseEntity<List<User>> getFollowers(@PathVariable String username){
        try {
            return new ResponseEntity<>(followRelationshipService.getFollowers(username), HttpStatus.OK);
        }catch(IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/checkFriendshipStatus/{username}")
    public ResponseEntity<Boolean> checkFriendshipStatus(@PathVariable String username){
        try {
            return new ResponseEntity<>(followRelationshipService.checkFriendshipStatus(username), HttpStatus.OK);
        }catch(IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
