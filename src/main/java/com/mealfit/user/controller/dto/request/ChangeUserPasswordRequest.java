package com.mealfit.user.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangeUserPasswordRequest {

    @Size(max = 20)
    @NotBlank(message = "비밀번호는 필수로 입력해주세요")
    private String password;

    @Size(max = 20)
    @NotBlank(message = "비밀번호 재확인을 필수로 입력해주세요")
    private String passwordCheck;
}
