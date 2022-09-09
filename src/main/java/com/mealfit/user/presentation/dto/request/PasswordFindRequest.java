package com.mealfit.user.presentation.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordFindRequest {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    public PasswordFindRequest(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
