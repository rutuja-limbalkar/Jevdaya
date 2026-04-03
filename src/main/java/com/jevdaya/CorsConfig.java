package com.jevdaya;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow your React frontend
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        
        // Allow all common methods including preflight
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // Allow all headers (important for Authorization, Content-Type etc.)
        configuration.setAllowedHeaders(List.of("*"));
        
        // Expose headers if needed (for JWT token in response)
        configuration.setExposedHeaders(List.of("Authorization"));
        
        // Allow credentials (set to true only if you use cookies later)
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}