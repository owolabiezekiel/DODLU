package com.dodlu.core.app.model.request.internship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class JobPostingApplicationRequest {
  @URL(message = "invalid url format for resume link")
  private String resumeLink;

  @URL(message = "invalid url format for resume link")
  private String coverLetterLink;

  @URL(message = "invalid url format for transcript link")
  private String transcriptLink;
}
