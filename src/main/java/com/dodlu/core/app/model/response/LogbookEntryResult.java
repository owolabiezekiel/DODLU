package com.dodlu.core.app.model.response;

import com.dodlu.core.domain.internship.model.LogbookEntryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class LogbookEntryResult extends PageImpl<LogbookEntryModel> {
  public LogbookEntryResult(Page<LogbookEntryModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
