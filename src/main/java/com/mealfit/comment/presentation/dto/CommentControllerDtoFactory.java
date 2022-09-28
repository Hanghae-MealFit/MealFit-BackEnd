package com.mealfit.comment.presentation.dto;

import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.presentation.dto.response.CommentResponse;

public class CommentControllerDtoFactory {

    public static CommentResponse commentResponse(Comment comment, Boolean liked) {
        return new CommentResponse(comment,
              comment.getUser().getUserProfile().getNickname(),
              comment.getUser().getUserProfile().getProfileImage(),
              liked);
    }
}
