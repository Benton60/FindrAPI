package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.exception.ObjectAlreadyExistsException;
import com.findr.FindrAPI.exception.ObjectDoesNotExistException;
import com.findr.FindrAPI.service.LikeRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/likes")
public class LikeRelationshipController {
    @Autowired
    private LikeRelationshipService likeRelationshipService;

    @PostMapping("/addLike/{postID}")
    public ResponseEntity<Post> addLike(@PathVariable long postID) {
        try{
            return new ResponseEntity<>(likeRelationshipService.addLike(postID), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (ObjectAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.ALREADY_REPORTED);
        }
    }
    @PostMapping("/removeLike/{postId}/")
    public ResponseEntity<Post> removeLike(@PathVariable long postId) {
        try {
            return new ResponseEntity<>(likeRelationshipService.removeLike(postId), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (ObjectDoesNotExistException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/checkLike/{postID}")
    public ResponseEntity<Boolean> checkLike(@PathVariable long postID) {
        try {
            return new ResponseEntity<>(likeRelationshipService.isLiked(postID), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
