package com.mealfit.user.domain;

import com.mealfit.common.email.EmailEvent;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import lombok.Getter;

@Getter
public class FindUsernameEvent extends EmailEvent {

    private static final EmailType emailType = EmailType.FIND_USERNAME;

    public FindUsernameEvent(String sendToUsername, String sendToEmail) {
        super(sendToUsername, sendToEmail, emailType);
    }
}
