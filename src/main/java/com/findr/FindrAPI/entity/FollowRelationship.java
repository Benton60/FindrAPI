package com.findr.FindrAPI.entity;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;


@Entity
public class FollowRelationship {
    @Column
    private Long followerId;
    @Column
    private Long followeeId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public FollowRelationship(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public FollowRelationship() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(Long followeeId) {
        this.followeeId = followeeId;
    }
}
