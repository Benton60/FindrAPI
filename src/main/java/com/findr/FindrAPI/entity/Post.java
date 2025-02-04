package com.findr.FindrAPI.entity;

import jakarta.persistence.*;

import java.awt.*;


@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column
    private String author;

    @Column (nullable = false, unique = true)
    private String photoPath;

    @Column
    private Point location;

    @Column
    private long likes;

    public Post(Long id, String description, String author, String photoPath, Point location, long likes) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.photoPath = photoPath;
        this.location = location;
        this.likes = likes;
    }
    public Post() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public static Point convertToPoint(String point){
        Point p = new Point(0,0);
        String[] split = point.replace("POINT(","").replace(")","").split(" ");
        p.x = (int)(Long.parseLong(split[0])*100000);
        p.y = (int)(Long.parseLong(split[1])*100000);
        return p;
    }
    public static String convertPointToString(Point p){
        return "POINT(" + p.x/100000 + " " + p.y/100000 + ")";
    }
}
