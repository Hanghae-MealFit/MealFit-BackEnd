package com.mealfit.user.controller;

import com.mealfit.user.dto.SignUpRequestDto;
import com.mealfit.user.service.UserSignUpService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserSignUpController {

    private final UserSignUpService userSignUpService;

    public UserSignUpController(UserSignUpService userSignUpService) {
        this.userSignUpService = userSignUpService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(HttpServletRequest request, @Valid SignUpRequestDto dto) {
        userSignUpService.signup(extractDomainRoot(request), dto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("가입 완료!");
    }

    private String extractDomainRoot(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI().toString(), "");
    }
}