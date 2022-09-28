package com.mealfit.post.domain;


import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostReadRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByIdLessThan(Long lastId, Pageable pageable);

    List<Post> findAllByIdOrderByLikeItDescViewDesc(Long postId);

    @Modifying
    @Query("update Post p set p.view = p.view + 1 where p.id = :id")
    int updateView(Long id);

    @Modifying
    @Query("update Post p set p.likeIt = p.likeIt + 1 where p.id = :postId")
    int plusLike(Long postId);

    @Modifying
    @Query("update Post p set p.likeIt = p.likeIt - 1 where p.id = :postId")
    int minusLike(Long postId);
}
