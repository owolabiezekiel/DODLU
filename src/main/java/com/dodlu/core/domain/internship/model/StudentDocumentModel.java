package com.dodlu.core.domain.internship.model;

import com.dodlu.core.domain.user.model.StudentProfileModel;
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
public class StudentDocumentModel {
  private String id;
  private StudentProfileModel student;
  private String resumeUrl;
  private String coverLetterUrl;
  private String transcriptUrl;
}
