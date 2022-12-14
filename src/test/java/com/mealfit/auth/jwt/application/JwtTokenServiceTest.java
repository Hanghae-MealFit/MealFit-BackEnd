package com.mealfit.auth.jwt.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.authentication.application.JwtTokenService;
import com.mealfit.authentication.application.JwtUtils;
import com.mealfit.authentication.application.dto.JwtTokenDto;
import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.JwtTokenType;
import com.mealfit.authentication.domain.JwtTokenVerifyResult;
import com.mealfit.authentication.domain.JwtTokenVerifyResult.TokenStatus;
import com.mealfit.authentication.domain.OAuthTokenRepository;
import com.mealfit.exception.authentication.InvalidTokenException;
import java.util.Optional;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private OAuthTokenRepository oAuthTokenRepository;

    @Getter
    class TokenInfo {
        private final String blackPrefix = "black_";
        private final String username = "username";
        private final String accessToken = "access_token";
        private final String refreshToken = "refresh_token";
        private final long accessExpiredTime = 10000;
        private final long refreshExpiredTime = 100000;

        JwtToken createAccessToken() {
            return new JwtToken(username, accessToken, accessExpiredTime, JwtTokenType.ACCESS);
        }

        JwtToken createExpiredToken() {
            return new JwtToken(username, accessToken, 0, JwtTokenType.ACCESS);
        }

        JwtToken createRefreshToken() {
            return new JwtToken(username, refreshToken, refreshExpiredTime, JwtTokenType.REFRESH);
        }

        JwtToken createBlackListToken() {
            return new JwtToken(blackPrefix + accessToken, "true", accessExpiredTime, JwtTokenType.BLACKLIST);
        }
    }


    @DisplayName("createAccessToken() ????????????")
    @Nested
    class Testing_createAccessToken {

        @DisplayName("username??? ??????????????? access token??? ????????????.")
        @Test
        void createAccessToken_with_username_success() {

            // given
            String username = "username";
            JwtToken accessToken = new TokenInfo().createAccessToken();

            given(jwtUtils.issueAccessJwtToken(anyString())).willReturn(accessToken);

            // when
            JwtTokenDto result = jwtTokenService.createAccessToken(username);

            // then
            assertThat(result.getUsername()).isEqualTo(accessToken.getUsername());
            assertThat(result.getToken()).isEqualTo(accessToken.getToken());

            verify(jwtUtils, times(1)).issueAccessJwtToken(anyString());
        }

        @DisplayName("username??? ???????????? ????????? NullPointException??? ????????????.")
        @Test
        void createAccessToken_no_username_fail() {

            // given
            String username = null;

            // whenthen
            assertThatThrownBy(() -> jwtTokenService.createAccessToken(username))
                  .isInstanceOf(NullPointerException.class);
        }
    }

    @DisplayName("blackAccessToken() ????????????")
    @Nested
    class Testing_blackAccessToken {

        @DisplayName("????????? ?????????????????? ????????????.")
        @Test
        void createAccessToken_with_username_success() {

            // given
            String tokenToBlackList = new TokenInfo().getAccessToken();
            JwtToken blackToken = new TokenInfo().createBlackListToken();

            given(jwtUtils.issueBlackListToken(anyString())).willReturn(blackToken);
            given(oAuthTokenRepository.findByKey(blackToken.getUsername()))
                  .willReturn(Optional.ofNullable(blackToken.getToken()));

            // when
            jwtTokenService.blackAccessToken(tokenToBlackList);
            jwtTokenService.isBlackListToken(blackToken.getUsername());

            // then
            verify(jwtUtils, times(1)).issueBlackListToken(anyString());
            verify(oAuthTokenRepository, times(1)).insert(any());
            verify(oAuthTokenRepository, times(1)).findByKey(anyString());
        }
    }

    @DisplayName("createRefreshToken() ????????????")
    @Nested
    class Testing_createRefreshToken {

        @DisplayName("username??? ??????????????? refresh token??? ????????????.")
        @Test
        void createRefreshToken_with_username_success() {

            // given
            TokenInfo tokenInfo = new TokenInfo();
            JwtToken refreshToken = tokenInfo.createRefreshToken();

            given(jwtUtils.issueRefreshJwtToken(anyString())).willReturn(refreshToken);
            given(oAuthTokenRepository.findByKey(refreshToken.getUsername()))
                  .willReturn(Optional.ofNullable(refreshToken.getToken()));

            // when
            jwtTokenService.createRefreshToken(tokenInfo.getUsername());
            String result = jwtTokenService.findByUsername(tokenInfo.getUsername());

            // then
            assertThat(result).isEqualTo(refreshToken.getToken());
            verify(oAuthTokenRepository, times(1)).insert(any());
            verify(oAuthTokenRepository, times(1)).findByKey(anyString());
        }

        @DisplayName("username??? ???????????? ????????? NullPointException??? ????????????.")
        @Test
        void createRefreshToken_no_username_fail() {

            // given
            String username = null;

            // when then
            assertThatThrownBy(() -> jwtTokenService.createRefreshToken(username))
                  .isInstanceOf(NullPointerException.class);
        }
    }

    @DisplayName("findByUsername() ????????????")
    @Nested
    class Testing_findByUsername {

        @DisplayName("???????????? ?????? ????????? InvalidTokenException??? ????????????.")
        @Test
        void findByUsername_no_key_fail() {

            // given
            String noUsername = "asdsad";
            given(oAuthTokenRepository.findByKey(anyString())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> jwtTokenService.findByUsername(noUsername))
                  .isInstanceOf(InvalidTokenException.class);
        }
    }

    @DisplayName("verifyToken() ????????????")
    @Nested
    class Testing_verifyToken {

        @DisplayName("???????????????????????? ????????? JWT ??????????????? ???????????? ?????????.")
        @Test
        void verifyToken_right_token_success() {

            // given
            TokenInfo tokenInfo = new TokenInfo();
            JwtToken accessToken = tokenInfo.createAccessToken();
            JwtTokenVerifyResult verifyResult = new JwtTokenVerifyResult(
                  tokenInfo.getUsername(), TokenStatus.AVAILABLE);

            // given
            given(jwtUtils.verifyToken(anyString())).willReturn(verifyResult);

            // when then
            JwtTokenVerifyResult result = jwtTokenService.verifyToken(accessToken.getToken());

            assertThat(result.getTokenStatus()).isEqualTo(TokenStatus.AVAILABLE);

            verify(jwtUtils, times(1)).verifyToken(anyString());
        }

        @DisplayName("????????? ??????????????? EXPIRED ???????????? ???????????????.")
        @Test
        void verifyToken_expired_token_send_EXPIRED() {

            // given
            TokenInfo tokenInfo = new TokenInfo();
            JwtToken expiredToken = tokenInfo.createExpiredToken();
            JwtTokenVerifyResult verifyResult = new JwtTokenVerifyResult(
                  tokenInfo.getUsername(), TokenStatus.EXPIRED);

            given(jwtUtils.verifyToken(anyString())).willReturn(verifyResult);

            // when then
            JwtTokenVerifyResult result = jwtTokenService.verifyToken(expiredToken.getToken());

            assertThat(result.getTokenStatus()).isEqualTo(TokenStatus.EXPIRED);
            verify(jwtUtils, times(1)).verifyToken(anyString());
        }

        @DisplayName("????????? ??????????????? DENIED ???????????? ???????????????.")
        @Test
        void verifyToken_deined_token_send_EXPIRED() {

            // given
            TokenInfo tokenInfo = new TokenInfo();
            JwtToken expiredToken = tokenInfo.createExpiredToken();
            JwtTokenVerifyResult verifyResult = new JwtTokenVerifyResult(
                  tokenInfo.getUsername(), TokenStatus.DENIED);

            given(jwtUtils.verifyToken(anyString())).willReturn(verifyResult);

            // when then
            JwtTokenVerifyResult result = jwtTokenService.verifyToken(expiredToken.getToken());

            assertThat(result.getTokenStatus()).isEqualTo(TokenStatus.DENIED);
            verify(jwtUtils, times(1)).verifyToken(anyString());
        }
    }
}
