package com.mealfit.comment.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.exception.comment.NoCommentContentException;
import com.mealfit.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String content, Long postId) {
        this.content = content;
        this.postId = postId;
        this.likeIt = 0;
    }

    @Builder
    @Generated
    public Comment(Long id, String content, int likeIt, Long postId, User user) {
        this.id = id;
        this.content = content;
        this.likeIt = likeIt;
        this.postId = postId;
        this.user = user;
    }

    public void settingUserInfo(User user) {
        this.user = user;
    }

    public void update(String comment) {
        if (comment == null) {
            throw new NoCommentContentException("댓글 내용이 비어있습니다.");
        }
        this.content = comment;
    }

    public void likeComment() {
        this.likeIt += 1;
    }

    public void unlikeComment() {
        this.likeIt -= 1;
    }
}

