package com.mealfit.diet.controller;

import com.mealfit.common.error.ErrorCode;
import com.mealfit.common.error.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.mealfit.diet.controller")
public class DietExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exception(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(ErrorResponse.of(ErrorCode.INVALID_CODE, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exception(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(ErrorResponse.of(ErrorCode.INVALID_CODE, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(
          MethodArgumentNotValidException exception,
          HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException 발생!!! url:{}, trace:{}", request.getRequestURI(),
              exception.getStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(makeErrorResponse(exception.getBindingResult()));
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        //에러가 있다면
        if(bindingResult.hasErrors()){
            String detail = bindingResult.getFieldError().getDefaultMessage();

            String bindResultCode = bindingResult.getFieldError().getCode();

            ErrorCode errorCode = ErrorCode.of(bindResultCode);
            return ErrorResponse.of(errorCode, detail);
        }

        return ErrorResponse.of(ErrorCode.INVALID_CODE);
    }
}