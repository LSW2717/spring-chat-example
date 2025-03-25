package com.dcool.springchatexample;

import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors-> cors
                    .configurationSource(corsConfigurationSource)
            )
            .headers(headers -> headers
                    .frameOptions(frameOptions -> frameOptions
                            .sameOrigin()
                    ))

            .authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
            )
            .oauth2ResourceServer(
                    oauth2->oauth2.jwt(Customizer.withDefaults())
           );
        ;
        return http.build();
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver r = new DefaultBearerTokenResolver();
        r.setAllowUriQueryParameter(true); //?access_token
        return r;
    }

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
    private String jwkSetUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        try{
            return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        }catch (Exception e){
            return new MockJwtDecoder();
        }
    }


    public static class MockJwtDecoder implements JwtDecoder {

        @Override
        public Jwt decode(String token) {
            return new Jwt(
                    token,
                    Instant.now(),
                    Instant.now().plusSeconds(30),
                    Map.of("alg", "none"),
                    Map.of("sub", token)
            );
        }
    }
}
