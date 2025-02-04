package com.findr.FindrAPI.repository;


import com.findr.FindrAPI.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
        SELECT id, title, content, ST_AsText(location) AS location,
               ST_Distance_Sphere(location, ST_GeomFromText(:point)) AS distance
        FROM posts
        ORDER BY distance
        LIMIT 20
        """, nativeQuery = true)
    List<Object[]> findNearestPosts(@Param("point") String point);
    Optional<Post> findById(Long id);
    List<Post> findByAuthor(String author);
}
