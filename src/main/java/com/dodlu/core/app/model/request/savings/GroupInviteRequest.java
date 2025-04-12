package com.dodlu.core.app.model.request.savings;

import jakarta.validation.constraints.NotEmpty;

public record GroupInviteRequest(
    @NotEmpty(message = "groupId is required") String groupId,
    @NotEmpty(message = "customerId is required") String customerId,
    boolean hasAdminRights) {}
