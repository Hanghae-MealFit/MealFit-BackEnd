package com.mealfit.exception.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode implements ErrorModel {

    // TOKEN
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "잘못된 토큰입니다."),

    // USER
    DUPLICATE_SIGNUP_INPUT(HttpStatus.BAD_REQUEST, "U001", "이미 해당 값이 존재합니다."),
    WRONG_SIGNUP_PASSWORD(HttpStatus.BAD_REQUEST, "U002", "비밀번호와 비밀번호 재확인을 확인해주세요."),
    BAD_LOGIN_INFO(HttpStatus.BAD_GATEWAY, "U003", "잘못된 로그인 정보입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "U004", "해당 작업을 하기 위한 권한이 없습니다."),

    // POST
    NO_POST_CONTENT(HttpStatus.BAD_REQUEST, "P001", "Content 값이 비어있습니다."),
    NO_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "P002", "이미지 업로드에 실패했습니다."),
    NOT_POST_WRITER(HttpStatus.UNAUTHORIZED, "P003", "게시글을 수정 및 삭제할 권한이 없습니다."),

    // COMMENT
    NO_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, "C001", "Content 값이 비어있습니다."),

    // COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "서버 내부 에러"),
    INVALID_CODE(HttpStatus.BAD_REQUEST, "C002", "Invalid Code"),
    RESOURCE_NOT_FOUND(HttpStatus.BAD_REQUEST, "C003", "찾고자 하는 정보가 없습니다."),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "C004", "Expired Code"),

    // EMAIL
    LIMIT_EMAIL_SEND(HttpStatus.BAD_REQUEST, "E001", "한계치 초과!"),
    BAD_VERIFY_CODE(HttpStatus.BAD_GATEWAY, "E002", "잘못된 인증 코드"),

    // AWS
    AWS_ERROR(HttpStatus.BAD_REQUEST, "A001", "aws client error"),

    // javax.validation
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "V001", "잘못된 입력값입니다.");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    public static ErrorCode of(String code) {
        switch (code) {
            case "Email":
            case "MIN":
            case "NotBlank":
            case "NotNull":
                return ErrorCode.INVALID_INPUT;

            default:
                throw new IllegalArgumentException("지원하지 않는 코드입니다.");
        }
    }

    @Override
    public String getKey() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.message;
    }
}