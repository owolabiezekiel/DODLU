package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_role")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class UserRole extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "user_id")
  private AppUser user;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;
}
