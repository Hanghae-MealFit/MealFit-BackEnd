package com.mealfit.user.application;

import com.mealfit.bodyInfo.domain.BodyInfo;
import com.mealfit.bodyInfo.domain.BodyInfoRepository;
import com.mealfit.common.event.infrastructure.EventsPublisher;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.DuplicatedUserException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.user.application.dto.UserServiceDtoFactory;
import com.mealfit.user.application.dto.request.ChangeFastingTimeRequestDto;
import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.request.CheckDuplicateSignupInputDto;
import com.mealfit.user.application.dto.request.FindPasswordRequestDto;
import com.mealfit.user.application.dto.request.FindUsernameRequestDto;
import com.mealfit.user.application.dto.request.UserSignUpRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.domain.FastingTime;
import com.mealfit.user.domain.FindPasswordEvent;
import com.mealfit.user.domain.FindUsernameEvent;
import com.mealfit.user.domain.Nutrition;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import com.mealfit.user.domain.UserStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;
    private final BodyInfoRepository bodyInfoRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,}$");

    @Transactional
    public UserInfoResponseDto signup(UserSignUpRequestDto dto) {
        validateSignUpDto(dto);

        User user = dto.toEntity();

        user.changePassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getProfileImage() != null) {
            List<String> profileUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "profile/");
            user.changeProfileImage(profileUrl.get(0));
        }

        User saveEntity = userRepository.save(user);

        bodyInfoRepository.save(
              BodyInfo.createBodyInfo(saveEntity.getId(), dto.getCurrentWeight(), LocalDate.now()));

        return UserServiceDtoFactory.userInfoResponseDto(saveEntity);
    }

    private void validateSignUpDto(UserSignUpRequestDto dto) {
        validateUsername(dto.getUsername());
        validateNickname(dto.getNickname());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword(), dto.getPasswordCheck());
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicatedUserException("이미 존재하는 아이디입니다.");
        }
    }

    private void validateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedUserException("이미 존재하는 닉네임입니다.");
        }
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedUserException("이미 존재하는 이메일입니다.");
        }
    }

    public void checkDuplicateSignupInput(CheckDuplicateSignupInputDto dto) {
        switch (dto.getKey()) {
            case "username":
                validateUsername(dto.getValue());
                break;
            case "email":
                validateEmail(dto.getValue());
                break;
            case "nickname":
                validateNickname(dto.getValue());
                break;
            default:
                throw new IllegalArgumentException("잘못된 값입니다");
        }
    }

    public void validatePassword(String password, String passwordCheck) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new PasswordCheckException(
                  "비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.");
        }
        if (!password.equals(passwordCheck)) {
            throw new PasswordCheckException("비밀번호와 비밀번호 재확인이 일치하지 않습니다.");
        }
    }

    @Transactional
    public UserInfoResponseDto changeUserInfo(ChangeUserInfoRequestDto dto) {
        User changeUser = findByUsername(dto.getUsername());

        if (dto.getProfileImage() != null) {
            String imageUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "profile/").get(0);
            changeUser.changeProfileImage(imageUrl);
        }

        // TODO: 하나의 업데이트 창에서 모두 변경하는 것이 아니기 때문에 추후 분리
        changeUser.changeNickname(dto.getNickname());

        changeUser.changeGoalWeight(dto.getGoalWeight());

        changeUser.changeFastingTime(new FastingTime(
              dto.getStartFasting(), dto.getEndFasting()));

        changeUser.changeNutrition(
              new Nutrition(dto.getKcal(),
                    dto.getCarbs(),
                    dto.getProtein(),
                    dto.getFat()));

        return UserServiceDtoFactory.userInfoResponseDto(changeUser);
    }

    @Transactional
    public UserInfoResponseDto fillSocialUserInfo(ChangeUserInfoRequestDto dto) {
        User changeUser = findByUsername(dto.getUsername());

        String imageUrl = null;
        if (dto.getProfileImage() != null) {
            imageUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "profile/").get(0);
            changeUser.changeProfileImage(imageUrl);
        }

        changeUser.changeNickname(dto.getNickname());

        changeUser.changeGoalWeight(dto.getGoalWeight());

        changeUser.changeFastingTime(new FastingTime(
              dto.getStartFasting(), dto.getEndFasting()));

        changeUser.changeNutrition(
              new Nutrition(dto.getKcal(),
                    dto.getCarbs(),
                    dto.getProtein(),
                    dto.getFat()));

        if (changeUser.isFirstLogin()) {
            changeUser.changeUserStatus(UserStatus.NORMAL);
        }

        return UserServiceDtoFactory.userInfoResponseDto(changeUser);
    }

    public UserInfoResponseDto showUserInfo(String username) {
        User user = findByUsername(username);

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    public List<UserInfoResponseDto> showUserInfoList() {
        return userRepository.findAll()
              .stream()
              .map(UserServiceDtoFactory::userInfoResponseDto)
              .collect(Collectors.toList());
    }

    @Transactional
    public UserInfoResponseDto changePassword(ChangeUserPasswordRequestDto dto) {
        validatePassword(dto.getChangePassword(), dto.getCheckPassword());

        User user = findByUsername(dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), user.getLoginInfo().getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getChangePassword());
        user.changePassword(encodedPassword);

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    public void findUsername(FindUsernameRequestDto dto) {
        User user = userRepository.findByEmail(dto.getSendingEmail())
              .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일입니다."));

        String maskedUsername = maskingUsername(user.getLoginInfo().getUsername());

        EventsPublisher.raise(new FindUsernameEvent(maskedUsername,
              dto.getSendingEmail()));
    }

    private String maskingUsername(String username) {
        String middleMask = username.substring(3);
        int size = middleMask.length();
        return username.replace(middleMask, "*".repeat(size));
    }

    @Transactional
    public void findPassword(FindPasswordRequestDto dto) {
        User user = findByUsername(dto.getUsername());

        if (!user.getUserProfile().getEmail().equals(dto.getSendingEmail())) {
            throw new IllegalArgumentException("잘못된 이메일입니다.");
        }

        String uuid = UUID.randomUUID().toString();

        String temporaryPassword = uuid.substring(0, 8);

        user.changePassword(passwordEncoder.encode(temporaryPassword));

        EventsPublisher.raise(new FindPasswordEvent(dto.getUsername(), dto.getSendingEmail(), temporaryPassword));
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
              .orElseThrow(() -> new UserNotFoundException("찾으려는 회원이 없습니다."));
    }

    @Transactional
    public UserInfoResponseDto changeNutrition(ChangeNutritionRequestDto dto) {
        User user = findByUsername(dto.getUsername());

        user.changeNutrition(
              new Nutrition(dto.getKcal(), dto.getCarbs(), dto.getProtein(), dto.getFat()));

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    @Transactional
    public UserInfoResponseDto changeFastingTime(ChangeFastingTimeRequestDto dto) {
        User user = findById(dto.getUserId());

        user.changeFastingTime(
              new FastingTime(dto.getStartFasting(), dto.getEndFasting())
        );

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
              .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    }


}
