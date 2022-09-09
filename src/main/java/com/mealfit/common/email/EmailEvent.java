package com.mealfit.common.email;

import com.mealfit.common.event.domain.Event;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import lombok.Getter;

@Getter
public class EmailEvent extends Event {

    private String sendToUsername;
    private String sendToEmail;
    private EmailType emailType;

    protected EmailEvent(String sendToUsername, String sendToEmail,
          EmailType emailType) {
        this.sendToUsername = sendToUsername;
        this.sendToEmail = sendToEmail;
        this.emailType = emailType;
    }
}
