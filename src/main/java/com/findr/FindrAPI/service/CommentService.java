package com.findr.FindrAPI.service;

import com.findr.FindrAPI.entity.Comment;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.CommentRepository;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment createComment(Comment comment) throws AuthenticationException {
        //this just makes sure that people cant pretend to be someone else to comment
        comment.setAuthor(getAuthenticatedUser().getUsername());
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostID(Long postID) {
        return commentRepository.findByPostID(postID).stream().map(r -> new Comment(
                ((Number) r[0]).longValue(),
                ((String) r[1]).trim(),
                ((String) r[2]).trim(),
                ((Number) r[3]).longValue()
        )).collect(Collectors.toList());
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
}
