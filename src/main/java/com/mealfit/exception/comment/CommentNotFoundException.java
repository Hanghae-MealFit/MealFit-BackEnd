package com.mealfit.exception.comment;

import com.mealfit.exception.wrapper.ErrorCode;

public class CommentNotFoundException extends CommentException{

    private static final ErrorCode errorCode = ErrorCode.COMMENT_NOT_FOUND;

    public CommentNotFoundException(String message) {
        super(errorCode, message);
    }
}
