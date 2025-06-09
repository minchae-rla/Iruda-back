package com.example.iruda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED))
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .requestMatchers("/api/oauth/kakao/callback").permitAll()
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
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(401, "Unauthorized")));

        return http.build();
    }
}
