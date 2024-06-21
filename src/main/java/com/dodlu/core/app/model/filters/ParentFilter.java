package com.dodlu.core.app.model.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentFilter {
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;
}
