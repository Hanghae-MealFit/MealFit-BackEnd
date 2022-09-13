package com.mealfit.exception.bodyInfo;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class BodyInfoException extends ApplicationException {

    protected BodyInfoException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
