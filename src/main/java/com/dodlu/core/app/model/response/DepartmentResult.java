package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.department.model.DepartmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class DepartmentResult extends PageImpl<DepartmentModel> {
  public DepartmentResult(Page<DepartmentModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
