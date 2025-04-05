package com.dodlu.core.app.model.request.systemsettings;

import jakarta.validation.constraints.NotNull;

public record SystemSettingsRequest(
    @NotNull(message = "maxWrongPinAttempts is required") Integer maxWrongPinAttempts,
    @NotNull(message = "minTransferAmount is required") Long minTransferAmount) {}
