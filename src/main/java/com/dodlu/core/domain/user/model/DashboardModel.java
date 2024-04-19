package com.dodlu.core.domain.user.model;

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
public class DashboardModel {
  private long departmentCount;
  private long facultyCount;
  private long activePlacementCount;
  private long totalPlacementCount;
  private long totalActiveStudents;
  private long registeredEmployers;
  private long totalCoordinators;
}
