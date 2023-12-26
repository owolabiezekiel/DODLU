package com.dodlu.core.persistence.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class UserProfile extends BaseEntity {
  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String username;

  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String profileImageUrl;
}
