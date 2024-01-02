package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.internship.model.JobPostingApplicationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class JobPostingApplicationResult extends PageImpl<JobPostingApplicationModel> {
  public JobPostingApplicationResult(Page<JobPostingApplicationModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
