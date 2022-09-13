package com.mealfit.comment.application.dto;

import com.mealfit.comment.application.dto.request.CommentDeleteRequestDto;
import com.mealfit.comment.application.dto.request.CommentLikeRequestDto;
import com.mealfit.comment.application.dto.request.CommentSaveRequestDto;
import com.mealfit.comment.application.dto.request.CommentUpdateRequestDto;
import com.mealfit.comment.presentation.dto.request.CreateCommentRequest;
import com.mealfit.comment.presentation.dto.request.UpdateCommentRequest;
import com.mealfit.user.domain.User;

public class CommentServiceDtoFactory {

    public static CommentSaveRequestDto commentCreateRequestDto(Long postId,
          User user,
          CreateCommentRequest request) {
        return new CommentSaveRequestDto(
              request.getContent(),
              postId,
              user.getId());
    }

    public static CommentUpdateRequestDto commentUpdateRequestDto(Long commentId,
          User user,
          UpdateCommentRequest request) {
        return new CommentUpdateRequestDto(request.getContent(),
              commentId,
              user.getId());
    }

    public static CommentDeleteRequestDto commentDeleteRequestDto(Long commentId, User user) {
        return new CommentDeleteRequestDto(commentId, user.getId());
    }

    public static CommentLikeRequestDto commentLikeRequestDto(Long commentId, User user) {
        return new CommentLikeRequestDto(commentId, user.getId());
    }
}
