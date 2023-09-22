package com.dodlu.core.domain.user.model;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class AppUserModel {
  private String id;
  private String email;
  private String username;
  private UserType type;
  private LocalDateTime lastLoginDate;
  private Set<RoleModel> role;
  private EntityStatus status;
}
