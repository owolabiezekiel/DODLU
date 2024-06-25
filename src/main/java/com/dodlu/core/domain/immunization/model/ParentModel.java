package com.dodlu.core.domain.immunization.model;

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
public class ParentModel {
  private String id;
  private String title;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String email;
  private LocalDate dateOfBirth;
  private String age;
  private Set<ChildModel> children;
}
