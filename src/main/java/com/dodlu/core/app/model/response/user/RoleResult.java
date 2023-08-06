package com.dodlu.core.app.model.response.user;

import com.dodlu.core.domain.user.model.RoleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class RoleResult extends PageImpl<RoleModel> {
  public RoleResult(Page<RoleModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
