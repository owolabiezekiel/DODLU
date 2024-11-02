package com.dodlu.core.domain.approval.model;

import com.dodlu.core.domain.approval.enums.ApprovalRequestType;
import com.dodlu.core.domain.approval.enums.ApprovalStatus;
import com.dodlu.core.domain.approval.enums.WorkflowType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ApprovalRequestModel(
    String id,
    String requesterId,
    String approverId,
    ApprovalStatus status,
    ApprovalRequestType type,
    WorkflowType workflowType,
    LocalDateTime requestDate,
    LocalDateTime approvalDate,
    LocalDateTime rejectionDate,
    String classPath,
    String requestObject) {}
