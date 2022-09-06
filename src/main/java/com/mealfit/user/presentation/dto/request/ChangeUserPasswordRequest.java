package com.mealfit.user.presentation.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeUserPasswordRequest implements Serializable {

    @Size(max = 20)
    @NotBlank(message = "비밀번호는 필수로 입력해주세요")
    private String password;

    @Size(max = 20)
    @NotBlank(message = "비밀번호는 필수로 입력해주세요")
    private String changePassword;

    @Size(max = 20)
    @NotBlank(message = "비밀번호 재확인을 필수로 입력해주세요")
    private String checkPassword;

    @Builder
    public ChangeUserPasswordRequest(String password, String changePassword, String checkPassword) {
        this.password = password;
        this.changePassword = changePassword;
        this.checkPassword = checkPassword;
    }
}
