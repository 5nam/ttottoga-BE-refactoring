package com.umc.ttg.global.config;

import com.umc.ttg.domain.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@RequiredArgsConstructor
public class WecSecurityConfig {

    private final AuthService authService;
    private final static String[] possiblePath = {};

    @Bean
    public WebSecurityCustomizer configure() {

        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(possiblePath);

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry
                        -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/login", "/signup", "user").permitAll()
                        .anyRequest().authenticated()
                ).build();
    }
}
