package com.mealfit.config.security.anonymous;

import com.mealfit.config.security.details.UserDetailsImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class CustomAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

    private static final String key = "ANONYMOUS_USER";

    public CustomAnonymousAuthenticationFilter() {
        super(key);
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {

        UserDetailsImpl anonymousUser = UserDetailsImpl.createAnonymousUser();

        return new AnonymousAuthenticationToken(key, anonymousUser, anonymousUser.getAuthorities());
    }
}
