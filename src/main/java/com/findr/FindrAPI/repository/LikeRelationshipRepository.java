package com.findr.FindrAPI.repository;

import com.findr.FindrAPI.entity.LikeRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRelationshipRepository extends JpaRepository<LikeRelationship, Long> {
    boolean existsByUserIDAndPostID(Long userID, Long postID);
    Optional<LikeRelationship> findByUserIDAndPostID(Long userID, Long postID);
}
