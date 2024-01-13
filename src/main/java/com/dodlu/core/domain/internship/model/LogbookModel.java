package com.dodlu.core.domain.internship.model;

import com.dodlu.core.domain.user.model.StudentProfileModel;
import java.time.LocalDateTime;
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
public class LogbookModel {
  private String id;
  private StudentProfileModel student;
  private int capacityInMonths;
  private LocalDateTime validUntil;
  private boolean active;
  private Integer supervisorGrade;
  private Integer itccGrade;
}
