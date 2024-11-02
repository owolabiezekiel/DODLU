package com.dodlu.core.persistence.approval.entity;

import com.dodlu.core.domain.approval.enums.ApprovalRequestType;
import com.dodlu.core.domain.approval.enums.ApprovalStatus;
import com.dodlu.core.domain.approval.enums.WorkflowType;
import com.dodlu.core.domain.approval.model.ApprovalRequestModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "approval_request")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class ApprovalRequest extends BaseEntity {
  @Column(nullable = false)
  private String requesterId;

  @Column(nullable = false)
  private String approverId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ApprovalStatus status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ApprovalRequestType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WorkflowType workflowType;

  @Column(nullable = false, updatable = false)
  private LocalDateTime requestDate;

  private LocalDateTime approvalDate;

  private LocalDateTime rejectionDate;

  @Column(nullable = false)
  private String classPath;

  @Column(nullable = false)
  private String requestObject;

  public ApprovalRequestModel toModel() {
    return ApprovalRequestModel.builder()
        .id(getId())
        .requesterId(requesterId)
        .approverId(approverId)
        .status(status)
        .type(type)
        .workflowType(workflowType)
        .approvalDate(approvalDate)
        .rejectionDate(rejectionDate)
        .classPath(classPath)
        .requestObject(requestObject)
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ApprovalRequest that = (ApprovalRequest) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
