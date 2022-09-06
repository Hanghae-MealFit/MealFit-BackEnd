package com.mealfit.config.security.formlogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.authentication.application.JwtTokenService;
import com.mealfit.authentication.application.dto.JwtTokenDto;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.dto.LoginRequestDto;
import com.mealfit.config.security.dto.LoginResponseDto;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenService jwtTokenService;

    public FormLoginFilter(AuthenticationManager authenticationManager,
          JwtTokenService jwtTokenService) {
        super(authenticationManager);
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
          HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(),
                  LoginRequestDto.class); //유저정보 담기

            UsernamePasswordAuthenticationToken authenticationToken =
                  new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());

            return getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 로그인 정보입니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
          HttpServletResponse response, FilterChain chain, Authentication authResult)
          throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        JwtTokenDto accessToken = jwtTokenService.createAccessToken(userDetails.getUsername());
        JwtTokenDto refreshToken = jwtTokenService.createRefreshToken(userDetails.getUsername());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken.getToken(),
              refreshToken.getToken(), userDetails);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
    }

    //로그인 실패시 예외 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
          HttpServletResponse response,
          AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(failed.getMessage());
    }
}
