package com.mealfit.exception.food;

import com.mealfit.exception.wrapper.ErrorCode;

public class FoodNotFoundException extends FoodException{

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public FoodNotFoundException(String message) {
        super(errorCode, message);
    }
}
