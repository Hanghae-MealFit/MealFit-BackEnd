package com.mealfit.config.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockGuestUserSecurityContextFactory implements
      WithSecurityContextFactory<WithGuestUser> {

    @Override
    public SecurityContext createSecurityContext(WithGuestUser guestUser) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken("GUEST_USER",
              null, grantedAuthorities);

        context.setAuthentication(anonymousAuthenticationToken);

        return context;
    }
}
