package com.mealfit.authentication.application;

import com.mealfit.authentication.application.dto.JwtTokenDto;
import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.JwtTokenVerifyResult;
import com.mealfit.authentication.domain.OAuthTokenRepository;
import com.mealfit.exception.authentication.InvalidTokenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtTokenService {

    private final OAuthTokenRepository tokenDao;
    private final JwtUtils jwtUtils;

    public JwtTokenService(OAuthTokenRepository tokenDao, JwtUtils jwtUtils) {
        this.tokenDao = tokenDao;
        this.jwtUtils = jwtUtils;
    }

    public JwtTokenDto createAccessToken(String username) {
        JwtToken jwtToken = jwtUtils.issueAccessJwtToken(username);
        return new JwtTokenDto(jwtToken.getUsername(), jwtToken.getToken());
    }

    public void blackAccessToken(String token) {
        JwtToken blackListToken = jwtUtils.issueBlackListToken(token);

        tokenDao.insert(blackListToken);
    }

    public JwtTokenDto createRefreshToken(String username) {
        JwtToken refreshToken = jwtUtils.issueRefreshJwtToken(username);
        tokenDao.insert(refreshToken);
        return new JwtTokenDto(refreshToken.getUsername(), refreshToken.getToken());
    }

    public String findByUsername(String username) {
        return tokenDao.findByKey(username)
              .orElseThrow(() -> new InvalidTokenException("해당하는 토큰이 없습니다."));
    }

    public boolean isBlackListToken(String accessToken) {
        return tokenDao.findByKey(accessToken).isPresent();
    }

    @Transactional(propagation = Propagation.NEVER)
    public JwtTokenVerifyResult verifyToken(String token) {
        return jwtUtils.verifyToken(token);
    }
}
