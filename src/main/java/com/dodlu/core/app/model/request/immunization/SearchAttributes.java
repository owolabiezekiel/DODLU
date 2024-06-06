package com.dodlu.core.app.model.request.immunization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAttributes {
  private String id;
  private String email;
  private String phoneNumber;
}
