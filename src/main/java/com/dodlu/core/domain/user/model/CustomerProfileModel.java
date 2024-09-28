package com.dodlu.core.domain.user.model;

import com.dodlu.core.domain.location.model.CountryModel;
import com.dodlu.core.domain.user.enums.Gender;
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
public class CustomerProfileModel {
  private String id;
  private String email;
  private String username;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String middleName;
  private Gender gender;
  private String profileImageUrl;
  private CountryModel country;
}
