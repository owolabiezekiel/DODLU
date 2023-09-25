package com.dodlu.core.domain.auth.concretes;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.domain.auth.abstracts.JwtService;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultJwtService implements JwtService {
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  @Override
  public CompletableFuture<String> generate(Authentication auth) {
    return supplyAsync(
        () -> {
          String scope =
              auth.getAuthorities().stream()
                  .map(GrantedAuthority::getAuthority)
                  .collect(Collectors.joining(" "));
          Instant now = Instant.now();
          Instant expirationTime = now.plus(Duration.ofHours(1));

          JwtClaimsSet claims =
              JwtClaimsSet.builder()
                  .issuer("self")
                  .issuedAt(now)
                  .expiresAt(expirationTime)
                  .subject(auth.getName())
                  .claim("roles", scope)
                  .build();

          return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        });
  }

  @Override
  public CompletableFuture<String> getUsernameFromToken(String token) {
    return supplyAsync(jwtDecoder.decode(token)::getSubject);
  }
}
