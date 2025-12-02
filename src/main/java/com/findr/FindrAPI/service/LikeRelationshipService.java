package com.findr.FindrAPI.service;

import com.findr.FindrAPI.entity.FollowRelationship;
import com.findr.FindrAPI.entity.LikeRelationship;
import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.exception.ObjectAlreadyExistsException;
import com.findr.FindrAPI.exception.ObjectDoesNotExistException;
import com.findr.FindrAPI.repository.LikeRelationshipRepository;
import com.findr.FindrAPI.repository.UserRepository;
import org.hibernate.Hibernate;
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
        User authUser = getAuthenticatedUser();


        //these two lines retrieve and modify the liked post
        Post tobeAdded = postService.findById(postID);
        tobeAdded.setLikes(tobeAdded.getLikes() + 1);

        if(likeRelationshipRepository.existsByUserIDAndPostID(getAuthenticatedUser().getId(), postID)) {
            throw new ObjectAlreadyExistsException();
        }

        likeRelationshipRepository.save(new LikeRelationship(tobeAdded.getId(), authUser.getId()));
        return postService.updatePostLikes(tobeAdded);
    }

    public Post removeLike(long postID) throws AuthenticationException, ObjectDoesNotExistException {
        User authUser = getAuthenticatedUser();


        Post tobeRemoved = postService.findById(postID);
        tobeRemoved.setLikes(tobeRemoved.getLikes() - 1);


        LikeRelationship like = likeRelationshipRepository
                .findByUserIDAndPostID(authUser.getId(), tobeRemoved.getId())
                .orElseThrow(ObjectDoesNotExistException::new);

        likeRelationshipRepository.delete(like);
        return postService.updatePostLikes(tobeRemoved);

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
