package com.mealfit.post.application.dto.request;

import lombok.Getter;

@Getter
public class PostDeleteReqeustDto {

    private Long postId;
    private Long userId;

    public PostDeleteReqeustDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
