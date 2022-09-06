package com.mealfit.authentication.infrastructure.repository;

import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.OAuthTokenRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!prod")
@Component
public class ConcurrentHashMapOAuthTokenRepository implements OAuthTokenRepository {

    private final ConcurrentHashMap<String, String> storage;

    public ConcurrentHashMapOAuthTokenRepository() {
        this(new ConcurrentHashMap<>());
    }

    private ConcurrentHashMapOAuthTokenRepository(ConcurrentHashMap<String, String> storage) {
        this.storage = storage;
    }

    public void insert(JwtToken jwtToken) {
        storage.put(jwtToken.getUsername(), jwtToken.getToken());
    }

    @Override
    public Optional<String> findByKey(String key) {
        return Optional.ofNullable(storage.get(key));
    }
}
