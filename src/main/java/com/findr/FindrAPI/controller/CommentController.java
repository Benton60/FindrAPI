package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.Comment;
import com.findr.FindrAPI.service.CommentService;
import com.findr.FindrAPI.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment,HttpStatus.CREATED);
    }


    @GetMapping("/byPost/{postID}")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable("postID") Long postID) {
        return new ResponseEntity<>(commentService.getAllComments(postID), HttpStatus.OK);
    }


    //ADD More endpoints
    //TODO -- byAuthor, delete,
}
