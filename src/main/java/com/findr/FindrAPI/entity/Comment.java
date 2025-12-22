package com.findr.FindrAPI.entity;


import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Long postID;


    public Comment(){}
    public Comment(Long id,  String comment, String author, Long postID) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.postID = postID;
    }

    private Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public Long getPostID() {
        return postID;
    }
    public void setPostID(Long postID) {
        this.postID = postID;
    }
}
