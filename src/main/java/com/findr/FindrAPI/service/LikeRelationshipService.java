package com.findr.FindrAPI.service;

import com.findr.FindrAPI.entity.FollowRelationship;
import com.findr.FindrAPI.entity.LikeRelationship;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.LikeRelationshipRepository;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class LikeRelationshipService {
    @Autowired
    private LikeRelationshipRepository likeRelationshipRepository;
    @Autowired
    private UserRepository userRepository;

    public LikeRelationship addRelationship(LikeRelationship likeRelationship) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();
        // Ensure the authenticated user is the follower
        if (!authenticatedUser.getId().equals(likeRelationship.getUserID())) {
            throw new AuthenticationException("You are not authorized to create this follow relationship.");
        }

        // Check if the relationship already exists
        boolean exists = likeRelationshipRepository.existsByUserIDAndPostID(
                likeRelationship.getUserID(),
                likeRelationship.getPostID()
        );

        if (exists) {
            throw new IllegalStateException("Follow relationship already exists.");
        }

        return likeRelationshipRepository.save(likeRelationship);

    }
    // Delete a follow relationship
    public void deleteRelationship(Long relationshipId) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();

        Optional<LikeRelationship> relationshipOpt = likeRelationshipRepository.findById(relationshipId);
        if (relationshipOpt.isEmpty()) {
            throw new IllegalArgumentException("Follow relationship not found.");
        }

        LikeRelationship relationship = relationshipOpt.get();

        // Ensure the authenticated user is the follower in this relationship
        if (!authenticatedUser.getId().equals(relationship.getUserID())) {
            throw new AuthenticationException("You are not authorized to delete this follow relationship.");
        }

        likeRelationshipRepository.delete(relationship);
    }
    public void deleteRelationshipByPostIdAndUserId(Long postID, Long userID) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();

        Optional<LikeRelationship> relationshipOpt = likeRelationshipRepository.findByUserIDAndPostID(userID, postID);
        if (relationshipOpt.isEmpty()) {
            throw new IllegalArgumentException("Follow relationship not found.");
        }

        LikeRelationship relationship = relationshipOpt.get();

        // Ensure the authenticated user is the follower in this relationship
        if (!authenticatedUser.getId().equals(relationship.getUserID())) {
            throw new AuthenticationException("You are not authorized to delete this follow relationship.");
        }

        likeRelationshipRepository.delete(relationship);
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
