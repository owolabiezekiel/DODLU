package com.dodlu.core.domain.location.model;

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
public class StateModel {
  private String id;
  private String name;
  private CountryModel country;
}
