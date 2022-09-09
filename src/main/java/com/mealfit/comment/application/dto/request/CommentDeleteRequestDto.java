package com.mealfit.comment.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDeleteRequestDto {

    private Long commentId;
    private Long userId;
}
