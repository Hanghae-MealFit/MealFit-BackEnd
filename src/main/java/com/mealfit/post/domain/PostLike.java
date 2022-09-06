package com.mealfit.post.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Table(name="post_like",
        indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "postId")
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;



    public PostLike toEntity (){
        PostLike postLike = new PostLike(id,postId,userId);
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        return postLike;
    }
}
