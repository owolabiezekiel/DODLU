package com.dodlu.core.domain.auth.abstracts;

import java.util.concurrent.CompletableFuture;
import org.springframework.security.core.Authentication;

public interface JwtService {
  CompletableFuture<String> generate(Authentication auth);
}
