package com.mealfit.exception.bodyInfo;

import com.mealfit.exception.wrapper.ErrorCode;

public class BodyInfoNotFoundException extends BodyInfoException{

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public BodyInfoNotFoundException(String message) {
        super(errorCode, message);
    }
}
