package com.dodlu.core.persistence.user.entity;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNullStream;
import static java.util.stream.Collectors.toSet;

import com.dodlu.core.domain.user.model.AppUserModel;
import com.dodlu.core.domain.user.model.EntityStatus;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.domain.user.model.UserType;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
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
@Table(name = "users")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class User extends BaseEntity {
  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String username;

  @Enumerated(EnumType.STRING)
  private UserType type;

  @Enumerated(EnumType.STRING)
  private EntityStatus status;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;

  private String password;
  private LocalDateTime lastLoginDate;

  private Set<RoleModel> getRoleModels() {
    return emptyIfNullStream(getRoles()).map(Role::toModel).collect(toSet());
  }

  public AppUserModel toModel() {
    return new AppUserModel()
        .setId(getId())
        .setEmail(getEmail())
        .setUsername(getUsername())
        .setType(getType())
        .setLastLoginDate(getLastLoginDate())
        .setStatus(getStatus())
        .setRole(getRoleModels());
  }
}
