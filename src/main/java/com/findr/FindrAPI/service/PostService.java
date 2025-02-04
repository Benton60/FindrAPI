package com.findr.FindrAPI.service;


import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Post> findByLocation(Point location) {
        String point = Post.convertPointToString(location);
        return postRepository.findNearestPosts(point)
                .stream()
                .map(result -> {
                    Long id = ((Number) result[0]).longValue();
                    String author = ((String) result[1]).trim();
                    String description = ((String) result[2]).trim();
                    String photoPath = ((String) result[3]).trim();
                    Point locat = Post.convertToPoint((String) result[3]);
                    Long likes = ((Number) result[4]).longValue();
                    return new Post(id, author, description, photoPath, locat, likes);
                }).collect(Collectors.toList());
    }
}



