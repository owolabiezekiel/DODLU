package com.dodlu.core.domain.user.model;

import com.dodlu.core.domain.user.enums.Industry;
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
public class EmployerProfileModel {
  private String id;
  private String email;
  private String username;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String profileImageUrl;
  private String companyName;
  private String vision;
  private String mission;
  private Industry industry;
}
