package com.dodlu.core.domain.user.model;

import com.dodlu.core.domain.user.enums.Industry;
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
public class CompanyModel {
  private String id;
  private String name;
  private String vision;
  private String mission;
  private Industry industry;
}
