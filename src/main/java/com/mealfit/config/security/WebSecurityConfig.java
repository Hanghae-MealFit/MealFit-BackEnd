package com.mealfit.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.mealfit.authentication.application.JwtTokenService;
import com.mealfit.config.security.OAuth.CustomOAuth2UserService;
import com.mealfit.config.security.OAuth.handler.OAuth2SuccessHandler;
import com.mealfit.config.security.anonymous.CustomAnonymousAuthenticationFilter;
import com.mealfit.config.security.formlogin.FormLoginFilter;
import com.mealfit.config.security.formlogin.FormLoginProvider;
import com.mealfit.config.security.formlogin.RestAuthenticationEntryPoint;
import com.mealfit.config.security.jwt.JwtAuthorizationFilter;
import com.mealfit.config.security.jwt.JwtAuthorizationProvider;
import com.mealfit.config.security.logout.CustomLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenService jwtTokenService;
    private final CustomLogoutHandler customLogoutHandler;
    private final FormLoginProvider formLoginProvider;
    private final JwtAuthorizationProvider jwtAuthProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    @Override // Bean 에 등록
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(formLoginProvider);
        auth.authenticationProvider(jwtAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.cors(withDefaults());

        // h2-console 설정
        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
              XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

        // jwt 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
              .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
              .antMatchers("/",
                    "/api/user/signup", "/api/user/verify", "/login", "/logout",
                    "/api/user/username/**", "/api/user/email/**", "/api/user/nickname/**", // 중복확인
                    "/api/user/find/**",
                    "/api/food/**",
                    "/h2-console/**",
                    "/test/error").permitAll()
              .antMatchers("/actuator/**").access("hasRole('ADMIN')")
              .antMatchers(HttpMethod.GET, "/api/post/**").permitAll()
              .anyRequest().authenticated();

        http.addFilterBefore(new FormLoginFilter(authenticationManager(), jwtTokenService),
                    UsernamePasswordAuthenticationFilter.class)
              .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), jwtTokenService),
                    UsernamePasswordAuthenticationFilter.class)
              .addFilter(new CustomAnonymousAuthenticationFilter());

        http.exceptionHandling()
              .accessDeniedHandler(new CustomAccessDeniedHandler())
              .authenticationEntryPoint(new RestAuthenticationEntryPoint());

        http.oauth2Login()
              .successHandler(oAuth2SuccessHandler)
              .userInfoEndpoint()
              .userService(customOAuth2UserService);

        http.logout()
              .logoutUrl("/logout")
              .addLogoutHandler(customLogoutHandler)
              .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
              .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
              .antMatchers("/h2-console/**");
    }

}
