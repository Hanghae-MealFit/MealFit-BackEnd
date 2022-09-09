package com.mealfit.exception.authentication;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class CustomAuthenticationException extends ApplicationException {

    protected CustomAuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
