package com.mealfit.user.application;

import com.mealfit.common.email.EmailEvent;
import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.FindPasswordEmail;
import com.mealfit.common.email.FindUsernameEmail;
import com.mealfit.common.email.VerifyEmail;
import com.mealfit.exception.email.BadVerifyCodeException;
import com.mealfit.exception.email.EmailSendCountLimitException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.user.application.dto.request.EmailAuthRequestDto;
import com.mealfit.user.domain.FindPasswordEvent;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import com.mealfit.user.domain.UserStatus;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class EmailEventHandler {

    private final String frontUrl;
    private final EmailUtil emailUtil;
    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, Integer> limitStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> verifyStorage = new ConcurrentHashMap<>();

    public EmailEventHandler(@Value("${common.front-url}")String frontUrl,
          EmailUtil emailUtil, UserRepository userRepository) {
        this.frontUrl = frontUrl;
        this.emailUtil = emailUtil;
        this.userRepository = userRepository;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendEmail(EmailEvent event) {
        int sendCountByUser = limitStorage.getOrDefault(event.getSendToEmail(), 0);

        checkEmailSendCount(sendCountByUser);

        limitStorage.put(event.getSendToEmail(), sendCountByUser + 1);

        String authKey = UUID.randomUUID().toString();

        verifyStorage.put(event.getSendToUsername(), authKey);

        switch (event.getEmailType()) {
            case VERIFY:
                emailUtil.sendEmail(event.getSendToEmail(),
                      new VerifyEmail(
                            frontUrl + "/user/verify"
                            , event.getSendToUsername(), authKey));
                return;

            case FIND_USERNAME:
                emailUtil.sendEmail(event.getSendToEmail(),
                      new FindUsernameEmail(frontUrl + "/user/login", event.getSendToUsername()));
                return;

            case FIND_PASSWORD:
                FindPasswordEvent findPasswordEvent = (FindPasswordEvent) event;
                emailUtil.sendEmail(findPasswordEvent.getSendToEmail(),
                      new FindPasswordEmail(frontUrl, findPasswordEvent.getSendToUsername(), findPasswordEvent.getTemporaryPassword()));
                return;

            default:
                throw new IllegalArgumentException("????????? ????????? ???????????????.");
        }
    }

    private static void checkEmailSendCount(int sendingCountByUser) {
        if (sendingCountByUser > 5) {
            throw new EmailSendCountLimitException("????????? ???????????? 5??? ?????? ????????? ??? ????????????.");
        }
    }

    @Transactional
    public void verifyEmail(EmailAuthRequestDto dto) {
        String verifyKey = verifyStorage.getOrDefault(dto.getUsername(), null);

        if (verifyKey == null) {
            throw new IllegalArgumentException("???????????? ??????????????? ??????????????????!");
        }

        if (!verifyKey.equals(dto.getAuthKey())) {
            throw new BadVerifyCodeException("????????? ?????? ??????????????????.");
        }

        User user = userRepository.findByUsername(dto.getUsername())
              .orElseThrow(() -> new UserNotFoundException("????????? ?????????????????????."));

        if (!user.isNotValid()) {
            throw new IllegalStateException("?????? ?????????????????????!");
        }

        verifyStorage.remove(dto.getUsername());

        user.changeUserStatus(UserStatus.FIRST_LOGIN);
    }

    @Scheduled(cron = "0 0 0 * * * ")
    public void clearLimitStorage() {
        limitStorage.clear();
    }
}
