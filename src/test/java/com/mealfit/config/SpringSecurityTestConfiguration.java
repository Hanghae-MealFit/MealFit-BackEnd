package com.mealfit.config;

import com.mealfit.common.factory.UserFactory;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class SpringSecurityTestConfiguration {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User normalUser = UserFactory.createSaveUser("user", "admin123",
              "user1", "user@gmail.com", UserStatus.NORMAL);
        UserDetailsImpl normalUserDetails = UserDetailsImpl.create(normalUser,
              Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        User adminUser = UserFactory.createSaveUser("admin", "admin123",
              "admin1", "admin@gmail.com", UserStatus.NORMAL);
        UserDetailsImpl adminUserDetails = UserDetailsImpl.create(adminUser,
              Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        return new InMemoryUserDetailsManager(Arrays.asList(normalUserDetails, adminUserDetails));
    }
}
