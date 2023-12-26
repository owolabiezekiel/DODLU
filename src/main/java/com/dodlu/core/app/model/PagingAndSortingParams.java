package com.dodlu.core.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingAndSortingParams {
  private String sort = "id";
  private String order = "desc";
  private int page = 0;
  private int size = 20;
}
