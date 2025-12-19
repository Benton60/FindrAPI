package com.findr.FindrAPI.service;

import com.findr.FindrAPI.entity.Comment;
import com.findr.FindrAPI.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments(Long postID) {
        return commentRepository.findByPostID(postID).stream().map(r -> new Comment(
                ((Number) r[0]).longValue(),
                ((String) r[1]).trim(),
                ((String) r[2]).trim(),
                ((Number) r[3]).longValue(),
                ((String) r[4]).trim()
        )).collect(Collectors.toList());
    }
}
