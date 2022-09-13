package com.mealfit.exception.diet;

import com.mealfit.exception.wrapper.ErrorCode;

public class DietNotFoundException extends DietException{

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public DietNotFoundException(String message) {
        super(errorCode, message);
    }
}
