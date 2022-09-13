package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class UserNotFoundException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public UserNotFoundException(String message) {
        super(errorCode, message);
    }
}