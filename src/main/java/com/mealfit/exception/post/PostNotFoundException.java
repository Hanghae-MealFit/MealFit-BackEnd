package com.mealfit.exception.post;

import com.mealfit.exception.wrapper.ErrorCode;

public class PostNotFoundException extends PostException{

    private static final ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;

    public PostNotFoundException(String message) {
        super(errorCode, message);
    }
}
