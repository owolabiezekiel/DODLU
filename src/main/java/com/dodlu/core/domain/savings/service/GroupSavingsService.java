package com.dodlu.core.domain.savings.service;

import static com.dodlu.core.app.utils.StringUtilities.extractFirstChars;
import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.domain.approval.enums.ApprovalRequestType.GROUP_INVITE;
import static com.dodlu.core.domain.approval.enums.ApprovalStatus.PENDING_USER_APPROVAL;
import static com.dodlu.core.domain.approval.enums.WorkflowType.INDIVIDUAL;
import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByEmail;
import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByUsername;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.savings.CreateGroupSavingRequest;
import com.dodlu.core.app.model.request.savings.GroupLoanRequest;
import com.dodlu.core.app.model.request.savings.GroupTransactionRequest;
import com.dodlu.core.domain.approval.service.ApprovalService;
import com.dodlu.core.domain.savings.enums.LoanStatus;
import com.dodlu.core.domain.savings.enums.RepaymentStatus;
import com.dodlu.core.domain.savings.model.GroupSavingLoanModel;
import com.dodlu.core.domain.savings.model.GroupSavingModel;
import com.dodlu.core.domain.transaction.enums.EntityType;
import com.dodlu.core.domain.transaction.enums.TransactionType;
import com.dodlu.core.persistence.approval.entity.ApprovalRequest;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.savings.entity.GroupSavingLoan;
import com.dodlu.core.persistence.savings.entity.GroupSavingUser;
import com.dodlu.core.persistence.savings.entity.GroupSetting;
import com.dodlu.core.persistence.savings.repository.GroupSavingLoanRepository;
import com.dodlu.core.persistence.savings.repository.GroupSavingRepository;
import com.dodlu.core.persistence.savings.repository.GroupSavingUsersRepository;
import com.dodlu.core.persistence.savings.repository.GroupSettingRepository;
import com.dodlu.core.persistence.transaction.entity.GroupBalance;
import com.dodlu.core.persistence.transaction.entity.Transaction;
import com.dodlu.core.persistence.transaction.repository.GroupBalanceRepository;
import com.dodlu.core.persistence.transaction.repository.TransactionRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupSavingsService {
  private final GroupSavingRepository groupSavingRepository;
  private final GroupSavingUsersRepository groupSavingUsersRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private final CoreExceptionService exceptionService;
  private final GroupBalanceRepository groupBalanceRepository;
  private final GroupSettingRepository groupSettingRepository;
  private final GroupSavingLoanRepository groupLoanRepository;
  private final TransactionRepository transactionRepository;
  private final ApprovalService approvalService;
  private final Gson gson;
  private final TransactionTemplate transactionTemplate;

  @Transactional
  public CompletableFuture<GroupSavingModel> createGroupSaving(
      User user, CreateGroupSavingRequest request) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              customerProfileRepository
                  .findByEmailEqualsIgnoreCase(user.getEmail())
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateBadRequest(
                              "Customer profile does not exist"));

          GroupSaving groupSaving =
              GroupSaving.builder()
                  .name(request.getName())
                  .description(request.getDescription())
                  .url(request.getImageUrl())
                  .leastSavingAmount(request.getSavingAmount())
                  .savingFrequency(request.getSavingFrequency())
                  .meetingLocation(request.getMeetingLocation())
                  .currency(request.getCurrency())
                  .admin(customerProfile)
                  .build();

          groupSaving = groupSavingRepository.save(groupSaving);

          GroupSavingUser groupSavingUser =
              GroupSavingUser.builder().group(groupSaving).member(customerProfile).build();

          groupSavingUsersRepository.save(groupSavingUser);

          GroupBalance groupBalance = generateEmptyGroupBalance(groupSaving);
          groupBalance = groupBalanceRepository.save(groupBalance);

          GroupSetting groupSetting =
              GroupSetting.builder()
                  .groupSavings(groupSaving)
                  .minimumSavingAmount(request.getSavingAmount())
                  .savingFrequency(request.getSavingFrequency())
                  .build();
          groupSetting = groupSettingRepository.save(groupSetting);

          groupSaving.setMembers(Set.of(customerProfile));
          groupSaving.setBalance(groupBalance);
          groupSaving.setSetting(groupSetting);
          return groupSaving.toModel();
        });
  }

  @Transactional
  public CompletableFuture<GroupSavingModel> getGroupSaving(User user, String id) {
    return supplyAsync(
        () -> {
          GroupSaving groupSaving =
              groupSavingRepository
                  .findById(id)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException("Group saving not found"));

          Set<String> memberEmails =
              groupSaving.getMembers().stream()
                  .map(CustomerProfile::getEmail)
                  .collect(Collectors.toSet());
          if (user.getEmail().equalsIgnoreCase(groupSaving.getAdmin().getEmail())
              || memberEmails.contains(user.getEmail())) {
            return groupSaving.toModel();
          } else {
            return null;
          }
        });
  }

  @Transactional
  public CompletableFuture<Page<GroupSavingModel>> getUserGroupSavings(User user, Pageable page) {
    return supplyAsync(
        () -> {
          Page<GroupSavingUser> groupSavingUserPage =
              groupSavingUsersRepository.getUserGroupSavings(user.getEmail(), page);
          List<GroupSavingModel> groupSavingModels =
              groupSavingUserPage.getContent().stream()
                  .map(g -> g.getGroup().toModel())
                  .distinct()
                  .collect(Collectors.toList());
          return new PageImpl<>(groupSavingModels, page, groupSavingUserPage.getTotalElements());
        });
  }

  @Transactional
  public CompletableFuture<Void> recordGroupTransaction(
      User user, String groupSavingId, GroupTransactionRequest request) {
    return runAsync(
        () -> {
          CustomerProfile customerProfile =
              customerProfileRepository
                  .findByEmailEqualsIgnoreCase(user.getEmail())
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateBadRequest(
                              "Customer profile does not exist"));

          GroupSaving groupSaving =
              groupSavingRepository
                  .findById(groupSavingId)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException("Group saving not found"));

          if (!customerProfile.getId().equals(groupSaving.getAdmin().getId())) {
            throw exceptionService.handleCreateUnAuthorized("Unauthorized!");
          }

          CustomerProfile member =
              groupSaving.getMembers().stream()
                  .filter(c -> c.getId().equals(request.getMemberId()))
                  .findFirst()
                  .orElse(null);
          if (member == null) {
            throw exceptionService.handleCreateBadRequest("Member not part of group");
          }

          Transaction transaction =
              Transaction.builder()
                  .senderId(request.getMemberId())
                  .senderName(extractFirstChars(customerProfile.getFullName(), 250))
                  .senderType(EntityType.INDIVIDUAL)
                  .receiverId(groupSavingId)
                  .receiverName(extractFirstChars(groupSaving.getName(), 250))
                  .receiverType(EntityType.GROUP)
                  .currency(groupSaving.getCurrency())
                  .amount(request.getAmount())
                  .reference("CTG-" + UUID.randomUUID())
                  .description(request.getDescription())
                  .status("Completed")
                  .type(TransactionType.GROUP_CONTRIBUTION)
                  .transactionDateTime(LocalDateTime.now())
                  .build();
          transactionRepository.save(transaction);

          GroupBalance balance = groupSaving.getBalance();
          balance.setAvailableBalance(balance.getAvailableBalance().add(transaction.getAmount()));
          groupBalanceRepository.save(balance);
        });
  }

  @Transactional
  public CompletableFuture<Void> inviteUserToGroup(User user, String groupId, String inviteeId) {
    return runAsync(
        () -> {
          GroupSaving groupSaving =
              groupSavingRepository
                  .findById(groupId)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException("Group saving not found"));

          if (!groupSaving.getAdmin().getEmail().equalsIgnoreCase(user.getEmail())) {
            log.info(
                "Unauthorized! User wants to invite someone to group but user is not group admin");
            throw exceptionService.handleCreateUnAuthorized("Unauthorized");
          }

          CustomerProfile customerProfile =
              customerProfileRepository
                  .findByUsernameOrEmailOrId(inviteeId)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException(
                              "Customer profile not found"));

          String adminId = groupSaving.getAdmin().getId();
          Set<String> memberIds =
              groupSaving.getMembers().stream()
                  .map(CustomerProfile::getId)
                  .collect(Collectors.toSet());
          if (memberIds.contains(customerProfile.getId())) {
            throw exceptionService.handleCreateBadRequest("User is already a member of this group");
          }

          ApprovalRequest approvalRequest =
              ApprovalRequest.builder()
                  .requesterId(adminId)
                  .approverId(customerProfile.getId())
                  .status(PENDING_USER_APPROVAL)
                  .type(GROUP_INVITE)
                  .workflowType(INDIVIDUAL)
                  .classPath(GroupSaving.class.getName())
                  .requestObject(gson.toJson(GroupSaving.builder().id(groupSaving.getId()).build()))
                  .build();
          approvalService.create(approvalRequest).join();
        });
  }

  @Transactional
  public CompletableFuture<Void> requestLoan(User user, GroupLoanRequest request) {
    return runAsync(
        () -> {
          GroupSaving groupSaving = getGroupSaving(request.getGroupId());
          CustomerProfile customerProfile = getCustomer(user.getEmail(), null);

          GroupSetting groupSetting = groupSaving.getSetting();
          if (Objects.isNull(groupSetting)) {
            throw exceptionService.handleCreateBadRequest(
                "Loan settings for this group does not exist. Please contact group administrator to update group settings");
          }

          Set<String> memberEmails = extractMemberEmails(groupSaving);
          if (!memberEmails.contains(customerProfile.getEmail())) {
            throw exceptionService.handleCreateBadRequest("User is not a member of this group");
          }

          BigDecimal highestAmount =
              calculatePercent(groupSaving.getBalance().getAvailableBalance(), 20.0F);
          if (request.getAmount().compareTo(highestAmount) > 0) {
            throw exceptionService.handleCreateBadRequest("Amount exceeds group loan limit");
          }

          Float interestPercent = groupSetting.getLoanInterestPercent();
          if (Objects.isNull(interestPercent)) {
            throw exceptionService.handleCreateBadRequest(
                "Interest percent missing on group setting. Please contact group administrator to update group settings");
          }
          BigDecimal interest = calculatePercent(request.getAmount(), interestPercent);
          GroupSavingLoan groupSavingLoan =
              GroupSavingLoan.builder()
                  .lendingGroup(groupSaving)
                  .borrower(customerProfile)
                  .loanStatus(LoanStatus.PENDING)
                  .repaymentStatus(RepaymentStatus.PENDING)
                  .amount(request.getAmount())
                  .interest(interest)
                  .repaymentAmount(request.getAmount().add(interest))
                  .amountRepaid(BigDecimal.ZERO)
                  .interestPercent(interestPercent)
                  .build();

          groupLoanRepository.save(groupSavingLoan);
        });
  }

  @Transactional
  public CompletableFuture<Page<GroupSavingLoanModel>> getGroupLoans(
      User user, String groupId, Pageable page) {
    return supplyAsync(
        () -> {
          GroupSaving groupSaving = getGroupSaving(groupId);

          Page<GroupSavingLoan> loanPage =
              groupLoanRepository.findAllByLendingGroup(groupSaving, page);
          List<GroupSavingLoanModel> loanModels =
              loanPage.getContent().stream()
                  .map(GroupSavingLoan::toModel)
                  .collect(Collectors.toList());
          return new PageImpl<>(loanModels, page, loanPage.getTotalElements());
        });
  }

  @Transactional
  public CompletableFuture<GroupSavingLoanModel> getGroupLoanById(User user, String groupLoanId) {
    return supplyAsync(
        () ->
            groupLoanRepository
                .findById(groupLoanId)
                .orElseThrow(
                    () ->
                        exceptionService.handleCreateNotFoundException("Group loan does not exist"))
                .toModel());
  }

  @Transactional
  public CompletableFuture<Void> approveLoan(User user, String groupLoanId) {
    return runAsync(
        () -> {
          GroupSavingLoan loan = getGroupSavingLoan(groupLoanId);

          if (!loan.getLendingGroup().getAdmin().getEmail().equalsIgnoreCase(user.getEmail())) {
            throw exceptionService.handleCreateUnAuthorized("Unauthorized");
          }

          if (loan.getLoanStatus() != LoanStatus.PENDING) {
            throw exceptionService.handleCreateBadRequest(
                "Action has been taken on this loan request. Please consider submitting another loan request");
          }

          GroupSaving groupSaving = loan.getLendingGroup();
          GroupBalance groupBalance = groupSaving.getBalance();
          CustomerProfile borrower = loan.getBorrower();

          BigDecimal highestAmount = calculatePercent(groupBalance.getAvailableBalance(), 20.0F);
          if (loan.getAmount().compareTo(highestAmount) > 0) {
            throw exceptionService.handleCreateBadRequest(
                "Amount exceeds group loan limit at this time");
          }

          Transaction transaction =
              Transaction.builder()
                  .senderId(groupSaving.getId())
                  .senderName(extractFirstChars(groupSaving.getName(), 250))
                  .senderType(EntityType.GROUP)
                  .receiverId(borrower.getId())
                  .receiverName(extractFirstChars(borrower.getFullName(), 250))
                  .receiverType(EntityType.INDIVIDUAL)
                  .currency(groupSaving.getCurrency())
                  .amount(loan.getAmount())
                  .reference("GTC-" + UUID.randomUUID())
                  .description("Loan disbursement")
                  .status("Completed")
                  .type(TransactionType.LOAN_DISBURSEMENT)
                  .transactionDateTime(LocalDateTime.now())
                  .build();

          BigDecimal newGroupBalance =
              groupBalance.getAvailableBalance().subtract(loan.getAmount());
          groupBalance.setAvailableBalance(newGroupBalance);

          loan.setLoanStatus(LoanStatus.APPROVED);
          loan.setApprovalDate(LocalDateTime.now());
          loan.setRepaymentStatus(RepaymentStatus.IN_PROGRESS);

          // TODO: Credit customer to be completed later
          log.info("Auto Customer credit not enabled at this time. Please disburse manually");

          transactionTemplate.executeWithoutResult(
              (n) -> {
                transactionRepository.save(transaction);
                groupBalanceRepository.save(groupBalance);
                groupLoanRepository.save(loan);
              });
        });
  }

  @Transactional
  public CompletableFuture<Void> rejectLoan(User user, String groupLoanId) {
    return runAsync(
        () -> {
          GroupSavingLoan loan = getGroupSavingLoan(groupLoanId);

          if (!loan.getLendingGroup().getAdmin().getEmail().equalsIgnoreCase(user.getEmail())) {
            throw exceptionService.handleCreateUnAuthorized("Unauthorized");
          }
          if (loan.getLoanStatus() != LoanStatus.PENDING) {
            throw exceptionService.handleCreateBadRequest(
                "Action has been taken on this loan request. Please consider submitting another loan request");
          }

          loan.setLoanStatus(LoanStatus.REJECTED);
          loan.setRepaymentStatus(RepaymentStatus.NOT_APPLICABLE);
          loan.setApprovalDate(LocalDateTime.now());
          transactionTemplate.executeWithoutResult((n) -> groupLoanRepository.save(loan));
        });
  }

  private GroupBalance generateEmptyGroupBalance(GroupSaving groupSaving) {
    return GroupBalance.builder()
        .groupSavings(groupSaving)
        .availableBalance(BigDecimal.ZERO)
        .build();
  }

  private Set<String> extractMemberEmails(GroupSaving groupSaving) {
    return groupSaving.getMembers().stream()
        .map(CustomerProfile::getEmail)
        .collect(Collectors.toSet());
  }

  private BigDecimal calculatePercent(BigDecimal value, Float percent) {
    return value.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100.0));
  }

  private CustomerProfile getCustomer(String email, String username) {
    return !isNullOrBlank(email)
        ? getCustomerByEmail(customerProfileRepository, exceptionService, email)
        : getCustomerByUsername(customerProfileRepository, exceptionService, username);
  }

  private GroupSaving getGroupSaving(String groupId) {
    return groupSavingRepository
        .findById(groupId)
        .orElseThrow(
            () -> exceptionService.handleCreateNotFoundException("Group saving does not exist"));
  }

  private GroupSavingLoan getGroupSavingLoan(String loanId) {
    return groupLoanRepository
        .findById(loanId)
        .orElseThrow(
            () -> exceptionService.handleCreateNotFoundException("Group loan does not exist"));
  }
}
