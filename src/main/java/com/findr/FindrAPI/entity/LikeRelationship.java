package com.findr.FindrAPI.entity;

import jakarta.persistence.*;


@Entity
public class LikeRelationship {
    @Column
    private long postID;
    @Column
    private long userID;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public LikeRelationship(long postID, long userID) {
        this.postID = postID;
        this.userID = userID;
    }
    public LikeRelationship() {}

    public long getPostID() {
        return postID;
    }
    public void setPostID(long postID) {
        this.postID = postID;
    }
    public long getUserID() {
        return userID;
    }
    public void setUserID(long userID) {
        this.userID = userID;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
