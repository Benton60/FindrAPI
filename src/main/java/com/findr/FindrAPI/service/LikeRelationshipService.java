package com.findr.FindrAPI.service;

import com.findr.FindrAPI.entity.FollowRelationship;
import com.findr.FindrAPI.entity.LikeRelationship;
import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.exception.ObjectAlreadyExistsException;
import com.findr.FindrAPI.exception.ObjectDoesNotExistException;
import com.findr.FindrAPI.repository.LikeRelationshipRepository;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class LikeRelationshipService {
    @Autowired
    private LikeRelationshipRepository likeRelationshipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;

    public Post addLike(long postID) throws AuthenticationException, ObjectAlreadyExistsException {
        //these two lines retrieve and modify the liked post
        Post tobeAdded = postService.findById(postID);
        tobeAdded.setLikes(tobeAdded.getLikes() + 1);

        if(likeRelationshipRepository.existsByUserIDAndPostID(getAuthenticatedUser().getId(), postID)) {
            throw new ObjectAlreadyExistsException();
        }

        try {
            likeRelationshipRepository.save(new LikeRelationship(tobeAdded.getId(), getAuthenticatedUser().getId()));
            return postService.updatePost(tobeAdded);

            //the catch clause goes back and deletes the saved relationship if the updatepost fails
        }catch (Exception e) {
            likeRelationshipRepository.delete(new LikeRelationship(tobeAdded.getId(), getAuthenticatedUser().getId()));
            throw e;
        }
    }

    public Post removeLike(long postID) throws AuthenticationException, ObjectDoesNotExistException {
        Post tobeRemoved = postService.findById(postID);
        tobeRemoved.setLikes(tobeRemoved.getLikes() - 1);


        if(!likeRelationshipRepository.existsByUserIDAndPostID(getAuthenticatedUser().getId(), postID)) {
            throw new ObjectDoesNotExistException();
        }
        try{
            likeRelationshipRepository.delete(new LikeRelationship(tobeRemoved.getId(), getAuthenticatedUser().getId()));
            return postService.updatePost(tobeRemoved);
        }catch (Exception e) {
            likeRelationshipRepository.save(new LikeRelationship(tobeRemoved.getId(), getAuthenticatedUser().getId()));
            throw e;
        }
    }

    public boolean isLiked(long postID) throws AuthenticationException {
        return likeRelationshipRepository.existsByUserIDAndPostID(getAuthenticatedUser().getId(), postID);
    }
    // Helper method to get the authenticated user
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
