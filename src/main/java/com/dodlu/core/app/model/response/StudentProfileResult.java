package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.user.model.StudentProfileModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class StudentProfileResult extends PageImpl<StudentProfileModel> {
  public StudentProfileResult(Page<StudentProfileModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
