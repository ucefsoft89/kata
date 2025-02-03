package com.alten.kata.config;

import com.alten.kata.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Value("${application.security.jwt.secret}")
    private String secret;
    @Value("${application.security.jwt.expiration}")
    private long expiration;
    private final JwtRequestFilter jwtRequestFilter;
    private static final String[] WHITE_LIST_URL = {
            "/account/token",
            "/account/login",
            "/account/register",
            "/v2/api-docs",        // Endpoint for Swagger API Docs
            "/swagger-resources/**", // Endpoint for Swagger Resources
            "/swagger-ui.html",     // Swagger UI HTML page
            "/webjars/**",         // Webjars for Swagger UI
            "/v3/api-docs/**",     // OpenAPI 3.0 API Docs
            "/swagger-ui/**"
    };
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(WHITE_LIST_URL).permitAll() // Allow unauthenticated access to these endpoints
                        .requestMatchers("/products/**").hasAuthority("ROLE_ADMIN") // Only admin can manage products
                        .anyRequest().authenticated() // Any other requests require authentication
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (use with caution)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public    JwtDecoder jwtDecoder (){
        var secretKey=new SecretKeySpec(secret.getBytes(),"");
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }



    @Bean
    public AuthenticationManager authManager(UserDetailsServiceImpl userDetailsServiceImpl) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        provider.setUserDetailsService(userDetailsServiceImpl);
         return new ProviderManager(provider);
    }
}