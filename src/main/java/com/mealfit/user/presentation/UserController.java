package com.mealfit.user.presentation;

import com.mealfit.bodyInfo.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.service.BodyInfoService;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.application.EmailService;
import com.mealfit.user.application.UserService;
import com.mealfit.user.application.dto.UserServiceDtoFactory;
import com.mealfit.user.application.dto.request.ChangeFastingTimeRequestDto;
import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.request.CheckDuplicateSignupInputDto;
import com.mealfit.user.application.dto.request.EmailAuthRequestDto;
import com.mealfit.user.application.dto.request.FindPasswordRequestDto;
import com.mealfit.user.application.dto.request.FindUsernameRequestDto;
import com.mealfit.user.application.dto.request.UserSignUpRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.presentation.dto.UserControllerDtoFactory;
import com.mealfit.user.presentation.dto.request.ChangeFastingTimeRequest;
import com.mealfit.user.presentation.dto.request.ChangeNutritionRequest;
import com.mealfit.user.presentation.dto.request.ChangeUserInfoRequest;
import com.mealfit.user.presentation.dto.request.ChangeUserPasswordRequest;
import com.mealfit.user.presentation.dto.request.UserSignUpRequest;
import com.mealfit.user.presentation.dto.response.UserInfoResponse;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final BodyInfoService bodyInfoService;
    private final EmailService emailService;

    public UserController(UserService userService, BodyInfoService bodyInfoService,
          EmailService emailService) {
        this.userService = userService;
        this.bodyInfoService = bodyInfoService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(HttpServletRequest httpServletRequest,
          @Valid UserSignUpRequest request) {
        UserSignUpRequestDto dto = UserServiceDtoFactory.userSignUpRequestDto(
              extractDomainRoot(httpServletRequest), request);
        userService.signup(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("가입 완료!");
    }

    @PostMapping("/social/signup")
    public ResponseEntity<String> socialSignup(@Valid ChangeUserInfoRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        ChangeUserInfoRequestDto dto = UserServiceDtoFactory.changeUserInfoRequestDto(
              userDetailsImpl.getUsername(), request);

        userService.fillSocialUserInfo(dto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료!");
    }

    @GetMapping("/{key}/{value}")
    public ResponseEntity<String> validateUsername(@PathVariable("key") String key,
          @PathVariable("value") String value) {
        CheckDuplicateSignupInputDto requestDto =
              UserServiceDtoFactory.checkDuplicateSignupInput(key, value);
        userService.checkDuplicateSignupInput(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> userInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        UserInfoResponseDto responseDto = userService.showUserInfo(userDetailsImpl.getUsername());
        UserInfoResponse response = UserControllerDtoFactory.userInfoResponse(responseDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    }

    // PUT -> multipart-form 의 경우 POST가 일반적.
    @PostMapping("/info")
    public ResponseEntity<String> changeUserInfo(@Valid ChangeUserInfoRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        ChangeUserInfoRequestDto requestDto = UserServiceDtoFactory.changeUserInfoRequestDto(
              userDetailsImpl.getUsername(), request);

        log.info("requestDto -> {}", requestDto.getNickname());

        userService.changeUserInfo(requestDto);

        //TODO: 무게가 info에 있어야 하나? -> 분리하자
        bodyInfoService.saveBodyInfo(userDetailsImpl.getUser(),
              new BodyInfoSaveRequestDto(requestDto.getCurrentWeight(), 0, LocalDate.now()));

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료");
    }

    /**
     * 이메일 인증
     */
    @GetMapping("/verify")
    public ResponseEntity<String> checkEmail(String username, String code) {
        EmailAuthRequestDto requestDto = UserServiceDtoFactory
              .emailAuthRequestDto(username, code);

        emailService.verifyEmail(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("이메일이 성공적으로 인증되었습니다.");
    }

    @GetMapping("/find/username")
    public ResponseEntity<String> findUsername(HttpServletRequest request, String email) {
        FindUsernameRequestDto requestDto = UserServiceDtoFactory.findUsernameRequestDto(
              extractDomainRoot(request), email);

        userService.findUsername(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("전송완료");
    }

    @GetMapping("/find/password")
    public ResponseEntity<String> findPassword(HttpServletRequest request,
          @RequestBody String email, String username) {
        FindPasswordRequestDto requestDto = UserServiceDtoFactory.findPasswordRequestDto(
              username, extractDomainRoot(request), email);

        userService.findPassword(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("전송완료");
    }

    @PutMapping("/password")
    public ResponseEntity<UserInfoResponse> changePassword(@Valid ChangeUserPasswordRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChangeUserPasswordRequestDto requestDto = UserServiceDtoFactory.changeUserPasswordRequestDto(
              userDetails.getUsername(), request);

        UserInfoResponseDto responseDto = userService.changePassword(requestDto);
        UserInfoResponse response = UserControllerDtoFactory.userInfoResponse(responseDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    }

    @PutMapping("/nutrition")
    public ResponseEntity<UserInfoResponse> changeNutrition(@Valid ChangeNutritionRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChangeNutritionRequestDto requestDto = UserServiceDtoFactory
              .changeNutritionRequestDto(userDetails.getUsername(), request);

        UserInfoResponseDto responseDto = userService.changeNutrition(requestDto);
        UserInfoResponse response = UserControllerDtoFactory.userInfoResponse(responseDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    }

    @PutMapping("/fastingTime")
    public ResponseEntity<UserInfoResponse> changeFastingTime(
          @Valid ChangeFastingTimeRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChangeFastingTimeRequestDto requestDto = UserServiceDtoFactory
              .changeFastingTimeRequestDto(userDetails.getUsername(), request);

        UserInfoResponseDto responseDto = userService.changeFastingTime(requestDto);
        UserInfoResponse response = UserControllerDtoFactory.userInfoResponse(responseDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    }

    private String extractDomainRoot(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI().toString(), "");
    }
}
