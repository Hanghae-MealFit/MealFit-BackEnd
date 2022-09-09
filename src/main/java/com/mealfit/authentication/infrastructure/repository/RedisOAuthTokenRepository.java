package com.mealfit.authentication.infrastructure.repository;

import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.OAuthTokenRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Profile("!prod")
@Component
public class RedisOAuthTokenRepository implements OAuthTokenRepository {

    private final ValueOperations<String, String> opsForValue;

    public RedisOAuthTokenRepository(StringRedisTemplate redisTemplate) {
        this.opsForValue = redisTemplate.opsForValue();
    }

    @Override
    public void insert(JwtToken jwtToken) {
        opsForValue.set(
              jwtToken.getUsername(),
              jwtToken.getToken(),
              jwtToken.getExpiredTime(),
              TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Optional<String> findByKey(String key) {
        return Optional.ofNullable(opsForValue.get(key));
    }

    @Override
    public void remove(String key) {
        opsForValue.getAndDelete(key);
    }
}
