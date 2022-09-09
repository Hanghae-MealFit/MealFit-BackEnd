package com.mealfit.authentication.domain;

import java.util.Optional;

public interface OAuthTokenRepository {

    void insert(JwtToken jwtToken);

    Optional<String> findByKey(String key);

    void remove(String key);
}
