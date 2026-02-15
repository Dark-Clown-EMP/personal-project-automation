package com.personalproject.demo.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // Import this
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Allow Actuator (Health checks) - Critical for Kubernetes/Cloud
                        .pathMatchers("/actuator/**").permitAll()
                        // Everything else requires a valid User
                        .anyExchange().authenticated()
                )
                // 1. Validates JWTs from Postman/Apps (Resource Server)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // 2. Redirects Browsers to Keycloak Login (OAuth2 Client) <--- ADD THIS
                .oauth2Login(Customizer.withDefaults());

        return httpSecurity.build();
    }
}