package ru.smmassistant.smmbackend.config.security;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.smmassistant.smmbackend.model.Role;

@Configuration
public class SecurityConfiguration {

    /* The client value used to allowed CORS */
    @Value("${client.url}")
    private String clientUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /* Converter for configuring Spring Security */
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        /* Connecting the Role Converter */
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());

        http
            /* Disabling the built-in protection against CSRF attacks, since it is used from OAuth2 */
            .csrf().disable()
            /* Allows to execute OPTIONS requests from the client (preflight requests) without authorization */
            .cors().and()
            /* Access to certain paths is granted, and the necessary Role to this path is specified.
             * The remaining paths are available only to authenticated clients
             */
            .authorizeHttpRequests(urlConfig -> urlConfig
                /* SpringDoc */
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                /* Application endpoints */
                .requestMatchers("/publication/**").hasAuthority(Role.USER.getAuthority())
                .requestMatchers("/register/**").hasAuthority(Role.USER.getAuthority())
                .anyRequest().authenticated())
            /* Enabling protection for OAuth 2, i.e. enabling accessToken verification */
            .oauth2ResourceServer()
            /* JWT is used to get accessToken */
            .jwt()
            /* Adding a role converter from JWT to Authorities */
            .jwtAuthenticationConverter(jwtAuthenticationConverter);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(clientUrl));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
