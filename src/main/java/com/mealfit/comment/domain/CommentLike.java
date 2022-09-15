package com.mealfit.comment.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;


@Table(name = "comment_like",
      indexes = {
            @Index(columnList = "userId"),
            @Index(columnList = "commentId")
      })
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long commentId;
}
