package com.dodlu.core.domain.department.model;

import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.domain.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
public class DepartmentModel {
  private String id;
  private String name;

  @JsonIncludeProperties({"email"})
  private UserModel hod;

  @JsonIncludeProperties({"email"})
  private UserModel coordinator;

  private FacultyModel faculty;
}
