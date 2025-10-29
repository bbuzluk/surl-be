package com.github.bbuzluk.surl.auth.config;

import com.github.bbuzluk.surl.auth.service.AuthTokenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final AuthTokenService authTokenService;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));
    http.sessionManagement(
        sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(
        authorizeHttpRequests -> {
          authorizeHttpRequests.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
          authorizeHttpRequests.requestMatchers("api/*/auth/**").permitAll();
          authorizeHttpRequests.anyRequest().authenticated();
        });

    http.addFilterBefore(
        new TokenAuthenticationFilter(authTokenService, userDetailsService),
        UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
