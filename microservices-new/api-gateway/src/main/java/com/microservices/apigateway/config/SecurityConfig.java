package com.microservices.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain springSecurityWebFilterChain(HttpSecurity serverHttpSecurity) throws Exception{

        serverHttpSecurity.csrf(csrf -> csrf
                .disable()
        );

        serverHttpSecurity.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/eureka/**").permitAll().anyRequest().authenticated()).oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

        return serverHttpSecurity.build();
    }
}
