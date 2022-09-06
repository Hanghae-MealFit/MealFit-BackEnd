package com.mealfit.exception.post;

import com.mealfit.exception.wrapper.ErrorCode;

public class NotPostWriterException extends PostException{

    private static final ErrorCode errorCode = ErrorCode.NOT_POST_WRITER;

    public NotPostWriterException(String message) {
        super(errorCode, message);
    }

}
