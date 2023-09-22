package com.dodlu.core.domain.auth.concretes;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.domain.auth.abstracts.JwtService;
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
          Instant now = Instant.now();
          String scope =
              auth.getAuthorities().stream()
                  .map(GrantedAuthority::getAuthority)
                  .collect(Collectors.joining(" "));

          System.out.println("got here: " + auth.getName());

          JwtClaimsSet claims =
              JwtClaimsSet.builder()
                  .issuer("self")
                  .issuedAt(now)
                  .subject(auth.getName())
                  .claim("roles", scope)
                  .build();

          return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        });
  }
}
