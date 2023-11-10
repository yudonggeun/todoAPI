package com.example.todo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(path ->
                        path
                                .requestMatchers("**")
                                .authenticated()
                                .requestMatchers("/auth/signup")
                                .permitAll()
                                .requestMatchers("/auth/login")
                                .permitAll()
                )
                .build();
    }
}
