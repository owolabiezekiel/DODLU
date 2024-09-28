package com.dodlu.core.persistence.auth.repository;

import com.dodlu.core.domain.user.enums.TokenType;
import com.dodlu.core.persistence.auth.entity.Token;
import com.dodlu.core.persistence.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByUserAndType(User user, TokenType type);
}
