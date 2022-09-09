package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class FindPasswordRequestDto {

    private String username;
    private String sendingEmail;

    public FindPasswordRequestDto(String username, String sendingEmail) {
        this.username = username;
        this.sendingEmail = sendingEmail;
    }
}
