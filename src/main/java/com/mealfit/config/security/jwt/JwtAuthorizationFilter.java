package com.mealfit.config.security.jwt;

import com.mealfit.authentication.application.JwtTokenService;
import com.mealfit.authentication.domain.JwtTokenVerifyResult;
import com.mealfit.authentication.domain.JwtTokenVerifyResult.TokenStatus;
import com.mealfit.authentication.application.dto.JwtTokenDto;
import com.mealfit.config.security.token.JwtAuthenticationToken;
import com.mealfit.exception.authentication.InvalidTokenException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
          JwtTokenService jwtTokenService) {
        super(authenticationManager);
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain chain)
          throws IOException, ServletException {

        String accessToken = null;

        try {
            accessToken = extractTokenFromHeader(request, HttpHeaders.AUTHORIZATION);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            chain.doFilter(request, response);
            return;
        }

        JwtTokenVerifyResult accessTokenVerifyResult = jwtTokenService.verifyToken(accessToken);

        switch (accessTokenVerifyResult.getTokenStatus()) {
            case AVAILABLE:
                // check logout
                if (jwtTokenService.isBlackListToken(accessToken)) {
                    throw new InvalidTokenException("이미 로그아웃 하셨습니다. 다시 로그인해 주세요.");
                } else {
                    // success filter
                    checkAndSaveToContextHolder(accessTokenVerifyResult.getUsername());
                }
                break;

            case EXPIRED:
                String refreshToken = extractTokenFromHeader(request, "refresh_token");
                JwtTokenVerifyResult refreshTokenVerifyResult = jwtTokenService.verifyToken(
                      refreshToken);

                if (refreshTokenVerifyResult.getTokenStatus() == TokenStatus.AVAILABLE) {
                    JwtTokenDto accessTokenDto = jwtTokenService.createAccessToken(
                          refreshTokenVerifyResult.getUsername());
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setHeader(HttpHeaders.AUTHORIZATION,
                          "Bearer " + accessTokenDto.getToken());

                    checkAndSaveToContextHolder(accessTokenDto.getUsername());
                } else {
                    // Access_Token + Refresh_Token 모두 유효하지 않은 토큰인 경우
                    throw new InvalidTokenException("유효하지 않은 JWT 발송 감지: " + accessToken);
                }
                break;

            default:
                throw new InvalidTokenException("정상적이지 않은 토큰입니다.");
        }
        chain.doFilter(request, response);
    }

    private void checkAndSaveToContextHolder(String username) {
        JwtAuthenticationToken jwtAuthToken = new JwtAuthenticationToken(username, null);
        Authentication authentication = getAuthenticationManager().authenticate(jwtAuthToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractTokenFromHeader(HttpServletRequest request, String tokenType) {
        String headerValue = request.getHeader(tokenType);

        //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인 (폼로그인필터에서 발급한 토큰 시크릿키 ->)
        if (headerValue == null
              || headerValue.isEmpty()
              || headerValue.isBlank()
              || !headerValue.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        return headerValue.substring("Bearer ".length());
    }
}
