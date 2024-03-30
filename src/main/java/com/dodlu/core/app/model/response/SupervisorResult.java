package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class SupervisorResult extends PageImpl<SupervisorProfileModel> {
  public SupervisorResult(Page<SupervisorProfileModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
