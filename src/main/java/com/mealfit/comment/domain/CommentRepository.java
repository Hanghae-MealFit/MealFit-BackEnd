package com.mealfit.comment.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAt(Long postId);

    List<Comment> findByUserIdOrderByCreatedAtDesc(Long UserId);
}

