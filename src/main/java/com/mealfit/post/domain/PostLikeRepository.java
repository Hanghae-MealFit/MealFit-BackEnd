package com.mealfit.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId,Long userId);

    void deleteByPostIdAndUserId(Long postId,Long userId);
}
