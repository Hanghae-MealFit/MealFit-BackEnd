package com.mealfit.unit.user.application;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.exception.email.EmailSendCountLimitException;
import com.mealfit.user.application.EmailEventHandler;
import com.mealfit.user.domain.SignupEvent;
import com.mealfit.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailEventHandlerTest {

    @InjectMocks
    private EmailEventHandler emailEventHandler;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtil emailUtil;

    @DisplayName("sendEmail() 메서드는")
    @Nested
    class Testing_sendEmail {

        @DisplayName("하루 이메일 한도를 넘기면 실패한다")
        @Test
        void sendEmail_over_limit_fail() {

            Assertions.assertThatThrownBy(() -> emailEventHandler
                        .sendEmail(new SignupEvent("username", "test@gmail.com")))
                  .isInstanceOf(EmailSendCountLimitException.class);
        }
    }

}
