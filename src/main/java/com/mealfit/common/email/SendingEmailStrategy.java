package com.mealfit.common.email;

import javax.mail.internet.MimeMessage;

@FunctionalInterface
public interface SendingEmailStrategy {

    void fillMessage(MimeMessage message);
}
