package com.findr.FindrAPI.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.findr.FindrAPI.serial.PointDeserializer;
import com.findr.FindrAPI.serial.PointSerializer;
import com.findr.FindrAPI.service.LocationService;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String author;

    @Column(nullable = false)
    private String description;

    @Column (nullable = false, unique = true)
    private String photoPath;

    @Column
    @JsonSerialize(using = PointSerializer.class)
    @JsonDeserialize(using = PointDeserializer.class)
    private Point location;

    @Column (nullable = false)
    private long likes;

    public Post(Long id, String author, String description, String photoPath, Point location, long likes) {
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
        if(point == null){
            return null;
        }
        String[] split = point.replace("POINT(","").replace(")","").split(" ");
        //parse the point long and lat and return a point object
        return new LocationService().createPoint((Double.parseDouble(split[0])),(Double.parseDouble(split[1])));
    }

    public static String convertPointToString(Point location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        System.out.println("POINT(" + (location.getX() + " " + (location.getY()) + ")"));
        return "POINT(" + (location.getX()) + " " + (location.getY()) + ")";
    }

}
