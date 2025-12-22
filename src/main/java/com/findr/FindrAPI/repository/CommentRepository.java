package com.findr.FindrAPI.repository;

import com.findr.FindrAPI.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostID(Long postID);
}
