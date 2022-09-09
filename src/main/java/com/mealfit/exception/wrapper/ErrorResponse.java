package com.mealfit.exception.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse implements Serializable {

    private HttpStatus httpStatus;
    private String code;
    private String message;
    private String detail;

    private ErrorResponse() {

    }

    private ErrorResponse(ErrorCode code) {
        this.httpStatus = code.getHttpStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    private ErrorResponse(ErrorCode code, String detail) {
        this.httpStatus = code.getHttpStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.detail = detail;
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }
}