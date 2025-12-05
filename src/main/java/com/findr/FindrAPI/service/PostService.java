package com.findr.FindrAPI.service;


import com.findr.FindrAPI.entity.LikeRelationship;
import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.LikeRelationshipRepository;
import com.findr.FindrAPI.repository.PostRepository;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.HandlerMapping;

import javax.naming.AuthenticationException;
import org.locationtech.jts.geom.Point;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.random.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public Post createPost(Post post) {
        //this doesn't let the client decide what the likes are for obvious reasons
        post.setLikes(0);
        //same with the author
        try {
            post.setAuthor(getAuthenticatedUser().getUsername());
        } catch (AuthenticationException e) {
            throw new IllegalStateException(e);
        }

        //
        post.setPhotoPath(String.valueOf(Math.random() * Math.random()));
        //save the post to get the database to generate an id
        Post savedPost = postRepository.save(post);

        //set the photo path based on the id.
        savedPost.setPhotoPath("/" + savedPost.getAuthor() + "/posts/" + savedPost.getId());

        //resave the post with the new photoPath
        return postRepository.save(savedPost);
    }

    public Post updatePost(Post post) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();
        System.out.println(authenticatedUser);
        if (Objects.equals(authenticatedUser.getUsername(), post.getAuthor())) {
            return postRepository.save(post);
        } else {
            throw new AuthenticationException();
        }
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
                    Point locat = Post.convertToPoint((String) result[4]);
                    Long likes = ((Number) result[5]).longValue();
                    return new Post(id, author, description, photoPath, locat, likes);
                }).collect(Collectors.toList());
    }

    private User getAuthenticatedUser() throws AuthenticationException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authenticatedUsername;

        if (principal instanceof UserDetails) {
            authenticatedUsername = ((UserDetails) principal).getUsername();
        } else {
            authenticatedUsername = principal.toString();
        }

        Optional<User> authenticatedUserOpt = userRepository.findByUsername(authenticatedUsername);
        if (authenticatedUserOpt.isEmpty()) {
            throw new AuthenticationException("Authenticated user not found.");
        }

        return authenticatedUserOpt.get();
    }

    //this function is needed because normally you cant update a post unless you own it but in the case of likes you can
    public Post updatePostLikes(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findByPage(int pageNum, Point location) {
        String point = Post.convertPointToString(location);
        List<Post> posts = postRepository.findNearestPostsByPage(pageNum * 20, point)
                .stream()
                .map(result -> {
                    Long id = ((Number) result[0]).longValue();
                    String author = ((String) result[1]).trim();
                    String description = ((String) result[2]).trim();
                    String photoPath = ((String) result[3]).trim();
                    Point locat = Post.convertToPoint((String) result[4]);
                    Long likes = ((Number) result[5]).longValue();
                    return new Post(id, author, description, photoPath, locat, likes);
                }).collect(Collectors.toList());
        try {
            return posts.subList(20 * pageNum - 1, posts.size());
        } catch (Exception e) {
            //this catches out of bounds errors which means we reached the end of the database
            return null;
        }
    }
}



