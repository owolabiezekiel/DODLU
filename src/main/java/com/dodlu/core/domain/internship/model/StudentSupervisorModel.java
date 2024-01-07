package com.dodlu.core.domain.internship.model;

import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
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
public class StudentSupervisorModel {
  private String id;
  private SupervisorProfileModel supervisor;
  private StudentProfileModel student;
  private boolean active;
}
