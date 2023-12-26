package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.internship.model.JobPostingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class JobPostingResult extends PageImpl<JobPostingModel> {
  public JobPostingResult(Page<JobPostingModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
