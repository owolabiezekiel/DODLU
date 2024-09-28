package com.dodlu.core.domain.user.model;

import com.dodlu.core.domain.department.model.DepartmentModel;
import com.dodlu.core.domain.location.model.CountryModel;
import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.enums.Level;
import com.dodlu.core.domain.user.enums.Programme;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
public class GeneralProfileModel {
  private String id;
  private String email;
  private String username;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String middleName;
  private String profileImageUrl;
  private CompanyModel company;
  private Gender gender;
  private String title;
  private Double cgpa;

  @JsonIncludeProperties({"name", "id"})
  private DepartmentModel department;

  private Programme programme;
  private Level level;
  private EmployerProfileModel employer;
  private CountryModel country;
}
