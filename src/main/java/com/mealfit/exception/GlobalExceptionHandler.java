package com.mealfit.exception;

import static java.util.Objects.requireNonNull;

import com.mealfit.exception.wrapper.ErrorCode;
import com.mealfit.exception.wrapper.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class: {}, Code: {}, Message: {}";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, e.getMessage());
        return ResponseEntity
              .status(errorCode.getHttpStatus())
              .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> dataAccessException(DataAccessException e) {
        log.error(LOG_FORMAT,
              e.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException e) {

        log.error(LOG_FORMAT,
              e.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * 개발자가 서비스 운용 중 의도한 오류들을 제외한 나머지 예외를 처리하는 메서드입니다.
     * @param e
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(
          MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorResponse.getCode(),
              errorResponse.getDetail());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errorResponse);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = requireNonNull(bindingResult.getFieldError());

            String detail = fieldError.getDefaultMessage();
            String bindResultCode = fieldError.getCode();

            ErrorCode errorCode = ErrorCode.of(bindResultCode);

            return ErrorResponse.of(errorCode, detail);
        }

        return ErrorResponse.of(ErrorCode.INVALID_CODE);
    }
}
