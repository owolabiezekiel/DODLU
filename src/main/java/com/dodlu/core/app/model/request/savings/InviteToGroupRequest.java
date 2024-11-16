package com.dodlu.core.app.model.request.savings;

import jakarta.validation.constraints.NotEmpty;
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
public class InviteToGroupRequest {
  @NotEmpty(message = "groupId is required")
  private String groupId;

  @NotEmpty(message = "inviteeId is required")
  private String inviteeId;
}
