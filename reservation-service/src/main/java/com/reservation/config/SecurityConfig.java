package com.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Désactive CSRF
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()  // Permet TOUTES les requêtes sans authentification
                );

        return http.build();
    }
}