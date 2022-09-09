package com.mealfit.authentication.application;

import com.mealfit.authentication.application.dto.JwtTokenDto;
import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.JwtTokenVerifyResult;
import com.mealfit.authentication.domain.OAuthTokenRepository;
import com.mealfit.exception.authentication.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class JwtTokenService {

    private final OAuthTokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    public JwtTokenService(OAuthTokenRepository tokenRepository, JwtUtils jwtUtils) {
        this.tokenRepository = tokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public JwtTokenDto createAccessToken(String username) {
        JwtToken jwtToken = jwtUtils.issueAccessJwtToken(username);
        return new JwtTokenDto(jwtToken.getUsername(), jwtToken.getToken());
    }

    public void blackAccessToken(String token) {
        log.info("black access token = {}", token);
        JwtToken blackListToken = jwtUtils.issueBlackListToken(token);

        tokenRepository.insert(blackListToken);
    }

    public void removeRefreshToken(String refreshToken) {
        JwtTokenVerifyResult jwtTokenVerifyResult = jwtUtils.verifyToken(refreshToken);
        String username = jwtTokenVerifyResult.getUsername();

        tokenRepository.remove(username);
    }


    public JwtTokenDto createRefreshToken(String username) {
        JwtToken refreshToken = jwtUtils.issueRefreshJwtToken(username);
        tokenRepository.insert(refreshToken);
        return new JwtTokenDto(refreshToken.getUsername(), refreshToken.getToken());
    }

    public String findByUsername(String username) {
        return tokenRepository.findByKey(username)
              .orElseThrow(() -> new InvalidTokenException("해당하는 토큰이 없습니다."));
    }

    public boolean isBlackListToken(String accessToken) {
        return tokenRepository.findByKey(accessToken).isPresent();
    }

    @Transactional(propagation = Propagation.NEVER)
    public JwtTokenVerifyResult verifyToken(String token) {
        return jwtUtils.verifyToken(token);
    }
}
