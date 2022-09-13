package com.mealfit.comment.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeRequestDto {

    private Long commentId;
    private Long userId;
}
