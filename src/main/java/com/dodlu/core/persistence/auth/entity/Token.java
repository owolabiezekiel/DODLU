package com.dodlu.core.persistence.auth.entity;

import com.dodlu.core.domain.user.enums.TokenType;
import com.dodlu.core.persistence.user.entity.User;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tokens")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Token implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false)
  private String code;

  @Enumerated(EnumType.STRING)
  private TokenType type;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime expiresAt;
}
