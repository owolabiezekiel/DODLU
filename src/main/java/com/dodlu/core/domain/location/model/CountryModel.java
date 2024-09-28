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
public class CountryModel {
  private String id;
  private String name;
  private String iso3Code;
  private String phoneExtensionCode;
}
