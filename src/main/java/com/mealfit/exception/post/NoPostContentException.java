package com.mealfit.exception.post;

import com.mealfit.exception.wrapper.ErrorCode;

public class NoPostContentException extends PostException{

    private static final ErrorCode errorCode = ErrorCode.NO_POST_CONTENT;

    public NoPostContentException(String message) {
        super(errorCode, message);
    }

}
