package com.dodlu.core.app.configuration.security;

import static com.dodlu.core.app.utils.Constants.*;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

import com.dodlu.core.app.model.response.ResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    try {
      jwt = authHeader.substring(7);
      username = jwtService.extractUsername(jwt);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        boolean isTokenValid = jwtService.isTokenValid(jwt, userDetails);
        log.info("Token is valid: {}", isTokenValid);
        if (isTokenValid) {
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          context.setAuthentication(authToken);
          SecurityContextHolder.setContext(context);
        } else {
          response.setHeader("Content-Type", "application/json");
          response.setStatus(HttpServletResponse.SC_CONFLICT);
          response.getWriter().write("{\"error\": \"Token is invalid or expired\"}");
          return;
        }
      }
      chain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      handleException(response, TOKEN_EXPIRED_MESSAGE);
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
      handleException(response, TOKEN_INVALID_MESSAGE);
    } catch (IllegalArgumentException e) {
      handleException(response, TOKEN_ILLEGAL_MESSAGE);
    }
  }

  private void handleException(HttpServletResponse response, String message) throws IOException {
    int statusCode = SC_UNAUTHORIZED;
    ResponseModel<Object> responseModel =
        new ResponseModel<>(null, message, Collections.singletonList(message), statusCode);

    response.setHeader("Content-Type", "application/json");
    response.setStatus(statusCode);
    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
    log.error("JWT error: {}::Status code: {}", message, statusCode);
  }
}
