package com.mealfit.config.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockGuestUserSecurityContextFactory.class)
public @interface WithGuestUser {

    String username() default "username";

    String nickname() default "nickname";

    String[] roles() default { "USER" };

    String[] authorities() default {};

    @AliasFor(annotation = WithSecurityContext.class)
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;

}