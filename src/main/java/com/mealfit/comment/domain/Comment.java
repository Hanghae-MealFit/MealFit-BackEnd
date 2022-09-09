package com.mealfit.comment.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int likeIt;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    public Comment(String content, Long postId) {
        this.content = content;
        this.postId = postId;
        this.likeIt = 0;
    }

    @Builder
    @Generated
    public Comment(Long id, String content, int likeIt, Long postId, Long userId) {
        this.id = id;
        this.content = content;
        this.likeIt = likeIt;
        this.postId = postId;
        this.userId = userId;
    }

    public void settingUserInfo(Long userId) {
        this.userId = userId;
    }

    public void update(String comment) {
        this.content = comment;
    }
}

