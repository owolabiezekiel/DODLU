package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByUsername(String username);
    Optional<Token> findByToken(String token);
    Optional<List<Token>> findAllByTimeToLiveIsBefore(Instant now);
}
