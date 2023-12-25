package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.faculty.model.FacultyModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class FacultyResult extends PageImpl<FacultyModel> {
  public FacultyResult(Page<FacultyModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
