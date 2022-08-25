package com.mealfit.user.service.dto.request;

import lombok.Getter;

@Getter
public class ChangeUserPasswordRequestDto {

    private String username;
    private String password;
    private String passwordCheck;

    public ChangeUserPasswordRequestDto(String username, String password, String passwordCheck) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }
}
