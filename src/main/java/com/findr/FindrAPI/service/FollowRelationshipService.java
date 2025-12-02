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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowRelationshipService {

    @Autowired
    private FollowRelationshipRepository followRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a follow relationship
    public FollowRelationship addFriend(String username) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();
        User tobeFriended = userRepository.findByUsername(username).get();

        // Check if the relationship already exists
        boolean exists = followRelationshipRepository.existsByFollowerIdAndFolloweeId(
                authenticatedUser.getId(),
                tobeFriended.getId()
        );

        if (exists) {
            throw new IllegalStateException("Follow relationship already exists.");
        }

        return followRelationshipRepository.save(new FollowRelationship(authenticatedUser.getId(), tobeFriended.getId()));
    }
    // Delete a follow relationship
    public void deleteFriend(String username) throws AuthenticationException {
        User authenticatedUser = getAuthenticatedUser();
        User tobeUnFriended = userRepository.findByUsername(username).get();

        if (!followRelationshipRepository.existsByFollowerIdAndFolloweeId(authenticatedUser.getId(), tobeUnFriended.getId())) {
            throw new IllegalArgumentException("Follow relationship not found.");
        }

        FollowRelationship followRelationship = followRelationshipRepository.findByFollowerIdAndFolloweeId(authenticatedUser.getId(), tobeUnFriended.getId());
        followRelationshipRepository.deleteById(followRelationship.getId());
    }
    public List<User> getFriends(String username) {
        Long userID;
        try {
            userID = userRepository.findByUsername(username).get().getId();
        } catch (Exception e) {
            throw new IllegalStateException("User not found.");
        }
        List<FollowRelationship> friends = followRelationshipRepository.getFollowRelationshipsByFollowerId(userID);
        List<User> friendList = new ArrayList<>();
        for (FollowRelationship friend : friends) {
            try {
                friendList.add(userRepository.findById(friend.getFolloweeId()).get());
            }catch(Exception e) {
                followRelationshipRepository.delete(friend);
            }
        }
        return friendList;
    }
    public List<User> getFollowers(String username) {
        Long userID;
        try {
            userID = userRepository.findByUsername(username).get().getId();
        } catch (Exception e) {
            throw new IllegalStateException("User not found.");
        }
        List<FollowRelationship> friends = followRelationshipRepository.getFollowRelationshipsByFolloweeId(userID);
        List<User> friendList = new ArrayList<>();
        for (FollowRelationship friend : friends) {
            try {
                friendList.add(userRepository.findById(friend.getFolloweeId()).get());
            }catch(Exception e) {
                followRelationshipRepository.delete(friend);
            }
        }
        return friendList;
    }
    public Boolean checkFriendshipStatus(String username) throws AuthenticationException {
        return followRelationshipRepository.existsByFollowerIdAndFolloweeId(getAuthenticatedUser().getId(), userRepository.findByUsername(username).get().getId());
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
