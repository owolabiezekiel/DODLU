package com.dodlu.core.persistence.approval.repository;

import com.dodlu.core.persistence.approval.entity.ApprovalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, String> {
  Page<ApprovalRequest> findAllByRequesterIdEqualsIgnoreCase(String username, Pageable pageable);

  Page<ApprovalRequest> findAllByApproverIdEqualsIgnoreCase(String username, Pageable pageable);
}
