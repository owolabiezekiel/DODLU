package com.dodlu.core.app.configuration.security;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.model.AuthUser;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
  private final UserRepository userRepository;
  private final CoreExceptionService exceptionService;
  private final RsaKeyProperties keys;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException {
    try {
      Optional<String> tokenOptional = extractToken(request);
      if (tokenOptional.isPresent()) {
        var token = tokenOptional.get();

        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();

        String authenticatedUsername = jwtDecoder.decode(token).getSubject();
        log.info("logged in username: {}", authenticatedUsername);

        User user =
            userRepository
                .findByUsernameIgnoreCase(authenticatedUsername)
                .orElseThrow(() -> exceptionService.handleCreateBadRequest("user not found"));

        UserDetails userDetails = AuthUser.build(user, token);

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      chain.doFilter(request, response);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials.");
    }
  }

  private Optional<String> extractToken(HttpServletRequest request) {
    String authorizationHeader = extractAuthorization(request);
    return extractToken(authorizationHeader);
  }

  public Optional<String> extractToken(String headerAuth) {
    return !isNullOrBlank(headerAuth) && headerAuth.startsWith("Bearer ")
        ? Optional.of(headerAuth.substring(7))
        : Optional.empty();
  }

  private String extractAuthorization(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }
}
