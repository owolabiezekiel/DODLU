package com.dodlu.core.app.model.request.discussion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class CreateCommentRequest {
  @NotBlank(message = "groupId is required")
  private String groupId;

  @NotBlank(message = "comment is required")
  private String comment;
}
