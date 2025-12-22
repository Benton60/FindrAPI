package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.Comment;
import com.findr.FindrAPI.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.createComment(comment);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/byPost/{postID}")
    public ResponseEntity<List<Comment>> getCommentsByPostID(@PathVariable("postID") Long postID) {
        return new ResponseEntity<>(commentService.getCommentsByPostID(postID), HttpStatus.OK);
    }


    //ADD More endpoints
    //TODO -- byAuthor, delete,
}
