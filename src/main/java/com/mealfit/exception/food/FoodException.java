package com.mealfit.exception.food;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class FoodException extends ApplicationException {

    protected FoodException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
