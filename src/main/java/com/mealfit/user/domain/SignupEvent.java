package com.mealfit.user.domain;


import com.mealfit.common.email.EmailEvent;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import lombok.Getter;

@Getter
public class SignupEvent extends EmailEvent {

    private static final EmailType emailType = EmailType.VERIFY;

    public SignupEvent(String sendToUsername, String sendToEmail) {
        super(sendToUsername, sendToEmail, emailType);
    }
}
