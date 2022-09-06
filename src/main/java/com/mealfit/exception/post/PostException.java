package com.mealfit.exception.post;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class PostException extends ApplicationException {

    protected PostException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
