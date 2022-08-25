package com.mealfit.config.security.dto;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@RedisHash("RefreshToken")
public class RefreshTokenRedis {
    @Id
    private String username;
    private String token;

    private RefreshTokenRedis(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public static RefreshTokenRedis createToken(String userId, String token){
        return new RefreshTokenRedis(userId, token);
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
