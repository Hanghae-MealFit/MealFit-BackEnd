package com.mealfit.admin.service;

import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.repository.CommentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VaadinCommentService implements VaddinService<Comment>{

    private final CommentRepository commentRepository;

    public VaadinCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findByPostId(Long id) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(id);
    }

    public List<Comment> findByUserId(Long id) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(id);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findByKeyAndValue(String key, String value) {
        return null;
    }

    @Override
    public List<Comment> findByKeyAndId(String key, Long id) {
        return null;
    }
}
