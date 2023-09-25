package com.dodlu.core.domain.model;

import static java.util.stream.Collectors.toList;

import com.dodlu.core.domain.user.model.EntityStatus;
import com.dodlu.core.domain.user.model.UserType;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Accessors(chain = true)
@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements UserDetails {
  private String id;
  private String username;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;
  private UserType userType;
  private EntityStatus status;

  @JsonIgnore @Transient private String password;

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

  public static AuthUser build(User user, String token) {
    List<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map((Role::getName))
            .map(String::toUpperCase)
            .map(SimpleGrantedAuthority::new)
            .collect(toList());

    return AuthUser.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(token)
        .authorities(authorities)
        .userType(user.getType())
        .status(user.getStatus())
        .build();
  }
}
