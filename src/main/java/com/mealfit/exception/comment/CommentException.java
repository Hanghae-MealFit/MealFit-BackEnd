package com.mealfit.exception.comment;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class CommentException extends ApplicationException {

    protected CommentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
