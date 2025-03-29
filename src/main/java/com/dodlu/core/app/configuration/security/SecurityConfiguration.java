package com.dodlu.core.app.configuration.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
  private final JWTAuthenticationFilter jwtAuthFilter;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  @Value("#{'${allowed.origin-patterns}'.split(',')}")
  private List<String> allowedOriginsPatterns;

  @Value("#{'${allowed.headers}'.split(',')}")
  private List<String> allowedHeaders;

  @Value("#{'${exposed.headers}'.split(',')}")
  private List<String> exposedHeaders;

  private static final List<String> HTTP_METHODS =
      List.of("POST", "PUT", "OPTIONS", "GET", "DELETE", "PATCH");

  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    var cors = new CorsConfiguration();
    cors.setAllowedOriginPatterns(allowedOriginsPatterns);
    cors.setAllowedHeaders(allowedHeaders);
    cors.setExposedHeaders(exposedHeaders);
    cors.setAllowedMethods(HTTP_METHODS);
    cors.setAllowCredentials(true);

    source.registerCorsConfiguration("/**", cors);
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth -> {
              auth.requestMatchers("/api/v1/auth/**").permitAll();
              auth.requestMatchers(HttpMethod.GET, "/api/v1/location/country").permitAll();
              auth.requestMatchers("/v1/health").permitAll();
              auth.requestMatchers("/actuator").permitAll();
              auth.requestMatchers("/actuator/prometheus").permitAll();
              auth.requestMatchers("/actuator/**").permitAll();
              auth.requestMatchers("/actuator/**").permitAll();
              auth.requestMatchers(HttpMethod.POST, "/v1/webhook/twilio/**").permitAll();

              auth.requestMatchers(
                      "/swagger**",
                      "/swagger-resources/**",
                      "/v2/api-docs",
                      "/v3/api-docs**",
                      "/v3/api-docs/**",
                      "/swagger-ui**",
                      "/swagger-ui/**",
                      "/csrf",
                      "/refresh",
                      "/webjars/**")
                  .permitAll();
              auth.anyRequest().authenticated();
            })
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
