package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class FindUsernameRequestDto {

    private String sendingEmail;

    public FindUsernameRequestDto(String sendingEmail) {
        this.sendingEmail = sendingEmail;
    }
}
