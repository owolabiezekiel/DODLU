package com.dodlu.core.domain.kyc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YouVerifyBvnResponse(
    boolean success, int statusCode, String message, BvnData data, List<String> links) {}
