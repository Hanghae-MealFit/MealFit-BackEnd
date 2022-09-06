package com.mealfit.comment.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAt(Long postId);

    List<Comment> findByUserIdOrderByCreatedAtDesc(Long UserId);

    @Modifying
    @Query("update Comment c set c.likeIt = c.likeIt + 1 where c.id = :commentId")
    int plusLike(Long commentId);

    @Modifying
    @Query("update Comment c set c.likeIt = c.likeIt - 1 where c.id = :commentId")
    int minusLike(Long commentId);
}

