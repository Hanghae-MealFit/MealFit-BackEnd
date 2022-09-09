package com.mealfit.config.security.logout;

import com.mealfit.authentication.application.JwtTokenService;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtTokenService jwtTokenService;

    public CustomLogoutHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) {

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            log.info(headerNames.nextElement());
        }

        String accessToken = extractTokenFromHeader(request, HttpHeaders.AUTHORIZATION);
        String refreshToken = extractTokenFromHeader(request, "refresh_token");

        log.info("accessToken: {} / refreshToken: {}", accessToken, refreshToken);

        jwtTokenService.blackAccessToken(accessToken);
        jwtTokenService.removeRefreshToken(refreshToken);
    }

    private String extractTokenFromHeader(HttpServletRequest request, String tokenType) {
        String headerValue = request.getHeader(tokenType);

        if (headerValue == null
              || headerValue.isEmpty()
              || headerValue.isBlank()
              || !headerValue.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        return headerValue.substring("Bearer ".length());
    }
}
