package com.mealfit.user.domain;

import com.mealfit.common.email.EmailEvent;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import lombok.Getter;

@Getter
public class FindPasswordEvent extends EmailEvent {

    private static final EmailType emailType = EmailType.FIND_PASSWORD;
    private final String temporaryPassword;

    public FindPasswordEvent(String sendToUsername, String sendToEmail, String temporaryPassword) {
        super(sendToUsername, sendToEmail, emailType);
        this.temporaryPassword = temporaryPassword;
    }
}
