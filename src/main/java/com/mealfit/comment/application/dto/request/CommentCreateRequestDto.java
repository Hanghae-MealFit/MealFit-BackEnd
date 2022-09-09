package com.mealfit.comment.application.dto.request;

import com.mealfit.comment.domain.Comment;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    private String content;
    private Long postId;
    private Long userId;

    public CommentCreateRequestDto(String content, Long postId, Long userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }

    public Comment toEntity() {
        Comment comment = new Comment(content, postId);
        comment.settingUserInfo(userId);
        return comment;
    }
}
