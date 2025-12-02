package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.service.LikeRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/removeLike/{postId}/")
    public ResponseEntity<Post> removeLike(@PathVariable long postId, @PathVariable long userID) {
        try {
            return new ResponseEntity<>(likeRelationshipService.removeLike(postId), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
