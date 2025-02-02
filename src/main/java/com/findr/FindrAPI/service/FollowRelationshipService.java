package com.findr.FindrAPI.service;

import com.findr.FindrAPI.entity.FollowRelationship;
import com.findr.FindrAPI.entity.User;
import com.findr.FindrAPI.repository.FollowRelationshipRepository;
import com.findr.FindrAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.AlreadyBuiltException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class FollowRelationshipService {

    @Autowired
    private FollowRelationshipRepository followRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a follow relationship
    public FollowRelationship addRelationship(FollowRelationship followRelationship) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();

        // Ensure the authenticated user is the follower
        if (!authenticatedUser.getId().equals(followRelationship.getFollowerId())) {
            throw new AuthenticationException("You are not authorized to create this follow relationship.");
        }

        // Check if the relationship already exists
        boolean exists = followRelationshipRepository.existsByFollowerIdAndFolloweeId(
                followRelationship.getFollowerId(),
                followRelationship.getFolloweeId()
        );

        if (exists) {
            throw new IllegalStateException("Follow relationship already exists.");
        }

        return followRelationshipRepository.save(followRelationship);
    }


    // Delete a follow relationship
    public void deleteRelationship(Long relationshipId) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();

        Optional<FollowRelationship> relationshipOpt = followRelationshipRepository.findById(relationshipId);
        if (relationshipOpt.isEmpty()) {
            throw new IllegalArgumentException("Follow relationship not found.");
        }

        FollowRelationship relationship = relationshipOpt.get();

        // Ensure the authenticated user is the follower in this relationship
        if (!authenticatedUser.getId().equals(relationship.getFollowerId())) {
            throw new AuthenticationException("You are not authorized to delete this follow relationship.");
        }

        followRelationshipRepository.delete(relationship);
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
