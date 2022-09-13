package com.mealfit.exception.diet;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class DietException extends ApplicationException {

    protected DietException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
