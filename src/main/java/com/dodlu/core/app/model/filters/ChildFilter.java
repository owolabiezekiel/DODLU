package com.dodlu.core.app.model.filters;

import com.dodlu.core.domain.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildFilter {
  private Gender gender;
  private String firstName;
  private String lastName;
}
