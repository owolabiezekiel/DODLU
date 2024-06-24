package com.dodlu.core.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminProfileModel {
  private String id;
  private String email;
  private String username;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String profileImageUrl;
}
