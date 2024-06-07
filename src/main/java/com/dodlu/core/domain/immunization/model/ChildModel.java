package com.dodlu.core.domain.immunization.model;

import com.dodlu.core.domain.user.enums.Gender;
import java.time.LocalDate;
import java.util.Set;
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
public class ChildModel {
  private String id;
  private Gender gender;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String age;
  private Set<ImmunizationModel> immunizations;
}
