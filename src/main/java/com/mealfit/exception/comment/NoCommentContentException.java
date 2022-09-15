package com.mealfit.exception.comment;

import com.mealfit.exception.wrapper.ErrorCode;

public class NoCommentContentException extends CommentException{

    private static final ErrorCode errorCode = ErrorCode.NO_COMMENT_CONTENT;

    public NoCommentContentException(String message) {
        super(errorCode, message);
    }
}
