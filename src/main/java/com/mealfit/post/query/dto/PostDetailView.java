package com.mealfit.post.query.dto;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import net.jcip.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Getter
@Entity
@Immutable
@Subselect(""
      + "SELECT p.id AS 'post_id',\n"
      + "p.user_id,\n"
      + "u.nickname,\n"
      + "u.profile_image,\n"
      + "p.content,\n"
      + "p.like_it,\n"
      + "p.view,\n"
      + "(select case when count(*) > 0 then TRUE\n"
      + "ELSE FALSE\n"
      + "        END\n"
      + "FROM post_like pl\n"
      + "        where pl.user_id = 1 and pl.post_id = p.id) as 'liked',\n"
      + "p.created_at,\n"
      + "p.updated_at\n"
      + "FROM post p inner JOIN users u\n"
      + "        on p.user_id = u.id;")
public class PostDetailView {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    private String content;

    private int like;

    private int view;

    private boolean liked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
