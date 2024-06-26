package com.dodlu.core.domain.immunization.model;

import com.dodlu.core.domain.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChildWithNoImmunoModel {
  private String id;
  private Gender gender;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String age;
}
