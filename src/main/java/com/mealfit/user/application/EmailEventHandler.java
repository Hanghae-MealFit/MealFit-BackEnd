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
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class EmailEventHandler {

    private final String frontUrl;
    private final EmailUtil emailUtil;
    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, Integer> limitStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> verifyStorage = new ConcurrentHashMap<>();

    public EmailEventHandler(@Value("${front-url}")String frontUrl,
          EmailUtil emailUtil, UserRepository userRepository) {
        this.frontUrl = frontUrl;
        this.emailUtil = emailUtil;
        this.userRepository = userRepository;
    }

    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendEmail(EmailEvent event) {
        int sendingCountByUser = limitStorage.getOrDefault(event.getSendToEmail(), 0);

        checkEmailSendCount(sendingCountByUser);

        limitStorage.put(event.getSendToEmail(), sendingCountByUser + 1);

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
                throw new IllegalArgumentException("잘못된 이메일 양식입니다.");
        }
    }

    private static void checkEmailSendCount(int sendingCountByUser) {
        if (sendingCountByUser > 5) {
            throw new EmailSendCountLimitException("하루에 이메일을 5건 이상 전송할 수 없습니다.");
        }
    }

    @Transactional
    public void verifyEmail(EmailAuthRequestDto dto) {
        String verifyKey = verifyStorage.getOrDefault(dto.getUsername(), null);

        if (verifyKey == null) {
            throw new IllegalArgumentException("이메일을 보내셨는지 확인해주세요!");
        }

        if (!verifyKey.equals(dto.getAuthKey())) {
            throw new BadVerifyCodeException("잘못된 인증 이메일입니다.");
        }

        User user = userRepository.findByUsername(dto.getUsername())
              .orElseThrow(() -> new UserNotFoundException("잘못된 인증정보입니다."));

        if (!user.isNotValid()) {
            throw new IllegalStateException("이미 인증하셨습니다!");
        }

        verifyStorage.remove(dto.getUsername());

        user.changeUserStatus(UserStatus.FIRST_LOGIN);
    }

    @Scheduled(cron = "0 0 0 * * * ")
    public void clearLimitStorage() {
        limitStorage.clear();
    }
}
