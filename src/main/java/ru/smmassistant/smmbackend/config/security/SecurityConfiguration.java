package ru.smmassistant.smmbackend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!test")
public class SecurityConfiguration {

    private static final String SMM_ASSISTANT_CRUD_AUTHORITY = "ADMIN";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(urlConfig -> urlConfig
                .requestMatchers("/api/v1").permitAll()
                .anyRequest().hasAuthority(SMM_ASSISTANT_CRUD_AUTHORITY));

        return httpSecurity.build();
    }
}
