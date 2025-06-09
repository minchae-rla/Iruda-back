package com.example.iruda.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")                      // API 경로에만 CORS 적용
                .allowedOrigins("http://localhost:5173")   // 프론트 URL
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)                     // 쿠키 허용
                .exposedHeaders("Authorization", "Set-Cookie");
    }
}
