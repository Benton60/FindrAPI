package com.findr.FindrAPI.service;


import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post findById(long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    public List<Post> findByAuthor(String author) {
        List<Post> postList = postRepository.findByAuthor(author);
        return postList;
    }
}
