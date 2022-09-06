package com.mealfit.exception.post;

import com.mealfit.exception.wrapper.ErrorCode;

public class NoPostImageException extends PostException{

    private static final ErrorCode errorCode = ErrorCode.NO_IMAGE;

    public NoPostImageException(String message) {
        super(errorCode, message);
    }
}
