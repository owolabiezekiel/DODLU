package com.dodlu.core.domain.internship.model;

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
public class LogbookEntryModel {
  private String id;
  private String logbookId;
  private String description;
  private LocalDateTime dateTime;
  private boolean approved;
  private String comment;
}
