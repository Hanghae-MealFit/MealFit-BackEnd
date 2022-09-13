package com.mealfit.comment.presentation.dto;

import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.presentation.dto.response.CommentResponse;
import com.mealfit.user.domain.User;

public class CommentControllerDtoFactory {

    public static CommentResponse commentResponse(User user, Comment comment,Boolean liked) {
        return new CommentResponse(comment,
              user.getUserProfile().getNickname(),
              user.getUserProfile().getProfileImage(),
                liked);
    }
}
