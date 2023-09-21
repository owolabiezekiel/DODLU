package com.dodlu.core.domain.user.model;

import java.time.LocalDate;
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
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private LocalDate dateOfBirth;
  private UserType type;
  private LocalDateTime lastLoginDate;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedDate;
  private String createdBy;
  private String modifiedBy;
  private Set<RoleModel> role;
  private EntityStatus status;
}
