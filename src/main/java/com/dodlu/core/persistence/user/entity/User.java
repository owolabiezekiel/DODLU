package com.dodlu.core.persistence.user.entity;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNullStream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.dodlu.core.domain.user.model.*;
import com.dodlu.core.persistence.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class User extends BaseEntity implements UserDetails {
  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String phoneNumber;

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

  @JsonIgnore @ToString.Exclude private String password;
  private LocalDateTime lastLoginDate;

  @Transient private List<GrantedAuthority> authorities;

  private Set<RoleModel> getRoleModels() {
    return emptyIfNullStream(getRoles()).map(Role::toModel).collect(toSet());
  }

  public UserModel toModel() {
    return new UserModel()
        .setId(getId())
        .setEmail(getEmail())
        .setUsername(getUsername())
        .setPhoneNumber(getPhoneNumber())
        .setType(getType())
        .setLastLoginDate(getLastLoginDate())
        .setStatus(getStatus())
        .setRole(getRoleModels());
  }

  public GeneralProfileModel toGeneralProfileModel() {
    return GeneralProfileModel.builder()
        .id(id)
        .email(getEmail())
        .phoneNumber(getPhoneNumber())
        .username(getUsername())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
        .map((Role::getName))
        .map(String::toUpperCase)
        .map(SimpleGrantedAuthority::new)
        .collect(toList());
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
