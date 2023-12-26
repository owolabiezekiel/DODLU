package com.dodlu.core.domain.user.model;

import com.dodlu.core.domain.department.model.DepartmentModel;
import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.enums.Level;
import com.dodlu.core.domain.user.enums.Programme;
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
public class StudentProfileModel {
  private String id;
  private String email;
  private String username;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private Gender gender;
  private String profileImageUrl;
  private Programme programme;
  private Level level;

  @JsonIncludeProperties({"name", "id"})
  private FacultyModel faculty;

  @JsonIncludeProperties({"name", "id"})
  private DepartmentModel department;
}
