package com.mealfit.post.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeRequestDto {

    private Long postId;
    private Long userId;
}
