package com.mealfit.authentication.infrastructure.repository;

import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.OAuthTokenRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Profile("!local")
@Primary
@Component
public class RedisOAuthTokenRepository implements OAuthTokenRepository {

    private final StringRedisTemplate redisTemplate;

    public RedisOAuthTokenRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void insert(JwtToken jwtToken) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(
              jwtToken.getUsername(),
              jwtToken.getToken(),
              jwtToken.getExpiredTime(),
              TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Optional<String> findByKey(String key) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return Optional.ofNullable(opsForValue.get(key));
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
