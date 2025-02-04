package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/byID/{id}")
    public ResponseEntity<Post> getPostByID(@PathVariable int id) {
        Post post = postService.findById(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/byAuthor/{author}")
    public ResponseEntity<List<Post>> getPostByAuthor(@PathVariable String author) {
        List<Post> postList = postService.findByAuthor(author);
        if (postList != null) {
            return new ResponseEntity<>(postList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/byLocation/{x}/{y}")
    public ResponseEntity<List<Post>> getPostByLocation(@PathVariable int x, @PathVariable int y) {
        List<Post> postList = postService.findByLocation(new Point(x,y));
        if (postList != null) {
            return new ResponseEntity<>(postList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
