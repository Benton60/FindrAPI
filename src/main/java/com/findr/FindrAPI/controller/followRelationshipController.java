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
public class followRelationshipController {
    @Autowired
    private FollowRelationshipService followRelationshipService;

    @PostMapping("/addFriend/{follower}/{followee}")
    public ResponseEntity<FollowRelationship> addFriend(@PathVariable Long follower, @PathVariable Long followee){
        try {
            followRelationshipService.addRelationship(new FollowRelationship(follower, followee));
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
    }
    @DeleteMapping("/removeFriend/{relationShipID}")
    public ResponseEntity<Boolean> removeFriend(@PathVariable Long relationShipID){
        try {
            followRelationshipService.deleteRelationship(relationShipID);
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
}
