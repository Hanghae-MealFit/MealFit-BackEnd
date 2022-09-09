package com.mealfit.exception.authentication;

import com.mealfit.exception.wrapper.ErrorCode;

public class UnAuthorizedUserException extends CustomAuthenticationException {

    private static final ErrorCode errorCode = ErrorCode.UNAUTHORIZED_USER;

    public UnAuthorizedUserException(String message) {
        super(errorCode, message);
    }
}
