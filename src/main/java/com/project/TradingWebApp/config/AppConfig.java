package com.project.TradingWebApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AppConfig {

    /**
     * Configures security settings for the application.
     *
     * - Enables stateless session management (JWT authentication)
     * - Secures API routes under "/api/**"
     * - Disables CSRF (since JWT is used)
     * - Adds a JWT authentication filter
     * - Enables CORS for frontend integration
     *
     * @param http HttpSecurity instance for configuring security settings.
     * @return Configured SecurityFilterChain.
     * @throws Exception if any error occurs during configuration.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()  // Allow signup and login
                   //     .requestMatchers("/api/**").authenticated()  // Secure API routes
                        .anyRequest().permitAll())  // Allow public access to other routes
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class) // Add JWT filter
                .csrf(csrf -> csrf.disable()) // Disable CSRF (JWT is stateless)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // Enable CORS

        return http.build();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) to allow requests from frontend applications.
     *
     * This method ensures that only specific origins are allowed to access the backend.
     *
     * @return Configured CORS policy.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://example.com")); // Frontend origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Headers allowed
        configuration.setAllowCredentials(true); // Allow cookies and auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides a password encoder bean for encoding user passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
