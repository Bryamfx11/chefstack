package com.chefstack.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

// GET publico; escritura y favoritos requieren login
@Configuration
public class SecurityConfig {

    private final FirebaseTokenVerifier verifier;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final List<String> origenesPermitidos;

    public SecurityConfig(FirebaseTokenVerifier verifier, RestAuthenticationEntryPoint authenticationEntryPoint,
                          @Value("${cors.allowed-origins}") String origenesPermitidos) {
        this.verifier = verifier;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.origenesPermitidos = Arrays.stream(origenesPermitidos.split(",")).map(String::trim).toList();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        // preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // docs y health
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/actuator/health").permitAll()
                        // lectura publica
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**", "/api/recetas/**",
                                "/api/ingredientes/**").permitAll()
                        // favoritos requieren login
                        .requestMatchers("/api/favoritos/**").authenticated()
                        // escritura requiere login
                        .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(new FirebaseAuthenticationFilter(verifier),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // cors para el frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origenesPermitidos);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
