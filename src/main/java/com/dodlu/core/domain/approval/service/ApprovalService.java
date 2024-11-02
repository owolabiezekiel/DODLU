package com.dodlu.core.domain.approval.service;

import static com.dodlu.core.app.utils.StringUtilities.stringsMatch;
import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByEmail;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.approval.enums.ApprovalRequestType;
import com.dodlu.core.domain.approval.enums.ApprovalStatus;
import com.dodlu.core.domain.approval.model.ApprovalRequestModel;
import com.dodlu.core.persistence.approval.entity.ApprovalRequest;
import com.dodlu.core.persistence.approval.repository.ApprovalRequestRepository;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.savings.entity.GroupSavingUser;
import com.dodlu.core.persistence.savings.repository.GroupSavingRepository;
import com.dodlu.core.persistence.savings.repository.GroupSavingUsersRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {
  private final ApprovalRequestRepository approvalRequestRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private final GroupSavingRepository groupSavingRepository;
  private final GroupSavingUsersRepository groupSavingUsersRepository;
  private final CoreExceptionService exceptionService;
  private final Gson gson;

  @Transactional
  public CompletableFuture<ApprovalRequestModel> create(ApprovalRequest request) {
    return supplyAsync(
        () -> {
          ensureRequesterNotApprover(request);
          request.setApprovalDate(null);
          request.setRequestDate(LocalDateTime.now());
          return approvalRequestRepository.save(request).toModel();
        });
  }

  @Transactional
  public CompletableFuture<ApprovalRequestModel> get(User user, String approvalRequestId) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByEmail(customerProfileRepository, exceptionService, user.getEmail());
          ApprovalRequest approvalRequest = getApprovalRequest(approvalRequestId);

          if (customerProfile.getId().equalsIgnoreCase(approvalRequest.getApproverId())
              || customerProfile.getId().equalsIgnoreCase(approvalRequest.getRequesterId())) {
            return approvalRequest.toModel();
          }

          return null;
        });
  }

  @Transactional
  public CompletableFuture<Page<ApprovalRequestModel>> getUserRequests(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByEmail(customerProfileRepository, exceptionService, user.getEmail());
          return approvalRequestRepository
              .findAllByRequesterIdEqualsIgnoreCase(customerProfile.getId(), pageable)
              .map(ApprovalRequest::toModel);
        });
  }

  @Transactional
  public CompletableFuture<Page<ApprovalRequestModel>> getApprovalRequests(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByEmail(customerProfileRepository, exceptionService, user.getEmail());
          return approvalRequestRepository
              .findAllByApproverIdEqualsIgnoreCase(customerProfile.getId(), pageable)
              .map(ApprovalRequest::toModel);
        });
  }

  @Transactional
  public CompletableFuture<Void> reject(User user, String approvalRequestId) {
    return runAsync(
        () -> {
          ApprovalRequest approvalRequest = getApprovalRequest(approvalRequestId);
          ensureApprovalInPendingState(approvalRequest.getStatus());

          CustomerProfile customerProfile =
              getCustomerByEmail(customerProfileRepository, exceptionService, user.getEmail());

          if (customerProfile.getId().equalsIgnoreCase(approvalRequest.getApproverId())
              || customerProfile.getId().equalsIgnoreCase(approvalRequest.getRequesterId())) {
            approvalRequest.setStatus(getRejectedBy(approvalRequest.getType()));
            approvalRequestRepository.save(approvalRequest);
          } else {
            throw exceptionService.handleCreateUnAuthorized(
                "User not authorized to reject request");
          }
        });
  }

  @Transactional
  public CompletableFuture<Void> approve(User user, String approvalRequestId) {
    return runAsync(
        () -> {
          ApprovalRequest approvalRequest = getApprovalRequest(approvalRequestId);
          ensureApprovalInPendingState(approvalRequest.getStatus());
          CustomerProfile customerProfile =
              getCustomerByEmail(customerProfileRepository, exceptionService, user.getEmail());
          switch (approvalRequest.getType()) {
            case GROUP_INVITE:
              handleGroupInviteApproval(approvalRequest, customerProfile);
              break;
            case LOAN:
              throw exceptionService.handleCreateBadRequest("Request type not supported yet");
            case UNKNOWN:
            default:
              throw exceptionService.handleCreateBadRequest("Unknown request type");
          }
        });
  }

  private void handleGroupInviteApproval(
      ApprovalRequest approvalRequest, CustomerProfile customerProfile) {
    if (!stringsMatch(approvalRequest.getApproverId(), customerProfile.getId())) {
      throw exceptionService.handleCreateUnAuthorized("User not authorized");
    }

    GroupSaving groupSaving = gson.fromJson(approvalRequest.getRequestObject(), GroupSaving.class);
    groupSaving = getGroupSaving(groupSaving.getId());

    Set<String> memberIds =
        groupSaving.getMembers().stream().map(CustomerProfile::getId).collect(Collectors.toSet());
    if (memberIds.contains(customerProfile.getId())) {
      throw exceptionService.handleCreateBadRequest("User already part of group savings");
    }

    GroupSavingUser groupSavingUser =
        GroupSavingUser.builder().group(groupSaving).member(customerProfile).build();
    groupSavingUsersRepository.save(groupSavingUser);
  }

  private ApprovalRequest getApprovalRequest(String approvalRequestId) {
    return approvalRequestRepository
        .findById(approvalRequestId)
        .orElseThrow(
            () ->
                exceptionService.handleCreateNotFoundException(
                    format("Approval request with id %s does not exist", approvalRequestId)));
  }

  private void ensureApprovalInPendingState(ApprovalStatus status) {
    if (!(ApprovalStatus.PENDING.equals(status)
        || ApprovalStatus.PENDING_USER_APPROVAL.equals(status)
        || ApprovalStatus.PENDING_GROUP_ADMIN_APPROVAL.equals(status))) {
      throw exceptionService.handleCreateBadRequest(
          "Action has previously been taken on this request");
    }
  }

  private ApprovalStatus getRejectedBy(ApprovalRequestType requestType) {
    return switch (requestType) {
      case LOAN -> ApprovalStatus.REJECTED_BY_GROUP_ADMIN;
      case GROUP_INVITE -> ApprovalStatus.REJECTED_BY_USER;
      default -> ApprovalStatus.REJECTED;
    };
  }

  private GroupSaving getGroupSaving(String groupSavingId) {
    return groupSavingRepository
        .findById(groupSavingId)
        .orElseThrow(
            () ->
                exceptionService.handleCreateNotFoundException(
                    "Group saving with id %s does not exist", groupSavingId));
  }

  private void ensureRequesterNotApprover(ApprovalRequest request) {
    if (request.getRequesterId().equalsIgnoreCase(request.getApproverId())) {
      throw exceptionService.handleCreateBadRequest("Requester cannot be same as Approver");
    }
  }
}
