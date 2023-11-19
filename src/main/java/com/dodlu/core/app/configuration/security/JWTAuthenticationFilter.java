package com.dodlu.core.app.configuration.security;

import static com.dodlu.core.domain.user.utils.FunctionalUtils.isNullOrEmpty;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final DodluUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain)
      throws IOException {
    try {
      Optional<String> tokenOptional = extractToken(request);
      if (tokenOptional.isPresent()) {
        String token = tokenOptional.get();
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.info("Token validity: {}", jwtService.isTokenValid(token, userDetails));
        Authentication auth =
            new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      chain.doFilter(request, response);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials.");
    }
  }

  public Optional<String> extractToken(HttpServletRequest request) {
    String authorizationHeader = extractAuthorization(request);
    return extractToken(authorizationHeader);
  }

  private String extractAuthorization(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }

  public Optional<String> extractToken(String headerAuth) {
    return !isNullOrEmpty(headerAuth) && headerAuth.startsWith("Bearer ")
        ? Optional.of(headerAuth.substring(7))
        : Optional.empty();
  }
}
