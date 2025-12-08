package com.findr.FindrAPI.repository;


import com.findr.FindrAPI.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
        SELECT id, author, description, photo_path, ST_AsText(location) AS location, likes, 
               ST_Distance_Sphere(location, ST_GeomFromText(:point)) AS distance
        FROM post
        ORDER BY distance
        LIMIT 20
        """, nativeQuery = true)
    List<Object[]> findNearestPosts(@Param("point") String point);


    @Query(value = """
    SELECT id, author, description, photo_path, ST_AsText(location) AS location, likes, 
           ST_Distance_Sphere(location, ST_GeomFromText(:point)) AS distance
    FROM post
    ORDER BY distance
    LIMIT 20 OFFSET :offset
    """, nativeQuery = true)
    List<Object[]> findNearestPostsByPage(@Param("offset") int offset, @Param("point") String point);



    Optional<Post> findById(Long id);
    List<Post> findByAuthor(String author);
}
