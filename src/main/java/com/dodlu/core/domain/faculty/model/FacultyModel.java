package com.dodlu.core.domain.faculty.model;

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
public class FacultyModel {
  private String id;
  private String name;

  @JsonIncludeProperties({"email"})
  private UserModel dean;
}
