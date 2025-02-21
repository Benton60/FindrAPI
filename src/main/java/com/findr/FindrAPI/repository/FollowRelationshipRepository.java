package com.findr.FindrAPI.repository;

import com.findr.FindrAPI.entity.FollowRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    List<FollowRelationship> getFollowRelationshipsByFollowerId(Long follower_ID);

    List<FollowRelationship> getFollowRelationshipsByFolloweeId(Long followee_ID);
}
