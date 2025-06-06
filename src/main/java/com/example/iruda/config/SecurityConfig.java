package com.example.iruda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(
                                        "/api/users/signup",
                                        "/api/users/login",
                                        "/api/users/idCheck",
                                        "/api/users/findId",
                                        "/api/users/findPw",
                                        "/api/users/setPw",
                                        "/api/auth/kakao/callback",
                                        "/api/projects/add",
                                        "/api/projects/getProject",
                                        "/api/projects/addTask/**",
                                        "/api/projects/search/**",
                                        "/api/projects/delete/**",
                                        "/api/projects/update/**",
                                        "/api/projects/deleteTask/**",
                                        "/api/projects/updateTask/**",
                                        "/api/projects/getTask/**",
                                        "/api/projects/inviteUser/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
