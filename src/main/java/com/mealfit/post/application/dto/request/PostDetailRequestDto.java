package com.mealfit.post.application.dto.request;

import lombok.Getter;

@Getter
public class PostDetailRequestDto {

    private Long postId;
    private Long userId;

    public PostDetailRequestDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
