package com.esprit.gitesprit.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Import for request matchers
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Only if using RequestLoggingFilter in a chain

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.stream.Stream; // For combining matchers

import static com.esprit.gitesprit.config.security.WhiteListConstants.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Log4j2
public class SecurityConfig {

    @Value("${application.cors.allowed-origins:*}")
    private List<String> allowedOrigins;

    public static final String[] GIT_PUBLIC_PATHS = {"/git/**", "/api/repos/**"};


    @Bean
    @Order(1)
    public SecurityFilterChain publicEndpointsSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring publicEndpointsSecurityFilterChain (Order 1) for Git and public API repo paths.");
        http
                .securityMatcher(GIT_PUBLIC_PATHS) // Apply this chain ONLY to these paths
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable) // Explicitly disable Basic Auth
                .formLogin(AbstractHttpConfigurer::disable); // Explicitly disable Form Login
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain jwtAuthorizationSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring jwtAuthorizationSecurityFilterChain (Order 2) for JWT protected API endpoints.");

        // Combine AUTH_WHITE_LIST with GIT_PUBLIC_PATHS to exclude them all from JWT auth
        // Or, more simply, rely on the fact that @Order(1) handles GIT_PUBLIC_PATHS.
        // The main thing is that this chain should NOT re-apply security to what @Order(1) made public.

        http
                // This chain should NOT apply to paths already handled by publicEndpointsSecurityFilterChain.
                // Spring Security processes chains in order. If a request matches a securityMatcher
                // in an earlier chain, subsequent chains with broader matchers (like a default "/**")
                // should not re-process it for authentication if the first chain fully handled it.
                // However, let's be explicit with authorization rules.

                .cors(Customizer.withDefaults()) // Apply global CorsFilter
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Whitelist for Swagger, custom auth endpoints, etc.
                        .requestMatchers(AUTH_WHITE_LIST).permitAll()
                        // Explicitly permit paths already handled by the first chain, just in case,
                        // though @Order and securityMatcher should prevent them from reaching here for auth.
                        // This is more of a "belt and suspenders" for the authorization part if a request somehow slipped through.
                        .requestMatchers(GIT_PUBLIC_PATHS).permitAll() // Technically redundant if @Order(1) works as expected
                        // All other requests require authentication via JWT
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
                )
                .addFilterAfter(new RequestLoggingFilter(), BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        log.info("Configuring CORS filter with allowed origins: {}", allowedOrigins);
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
         if (allowedOrigins.contains("*") && allowedOrigins.size() > 1) {
         log.warn("CORS: Using '*' with other specific origins. Consider refining allowedOrigins.");
         }
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}