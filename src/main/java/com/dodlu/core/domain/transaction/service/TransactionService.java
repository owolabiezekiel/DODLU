package com.dodlu.core.domain.transaction.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.BadRequestException;
import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.savings.model.GroupSavingModel;
import com.dodlu.core.domain.savings.service.GroupSavingsService;
import com.dodlu.core.domain.transaction.model.TransactionModel;
import com.dodlu.core.persistence.transaction.entity.Transaction;
import com.dodlu.core.persistence.transaction.repository.TransactionRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private final GroupSavingsService groupSavingsService;
  private final CoreExceptionService exceptionService;

  public CompletableFuture<Page<TransactionModel>> getCustomerTransactions(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              customerProfileRepository
                  .findByEmailEqualsIgnoreCase(user.getEmail())
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateBadRequest(
                              "Could not fetch customer details"));
          return getTransactions(customerProfile.getId(), pageable);
        });
  }

  public CompletableFuture<Page<TransactionModel>> getGroupTransactions(
      User user, String groupId, Pageable pageable) {
    return supplyAsync(
        () -> {
          GroupSavingModel groupSaving = groupSavingsService.getGroupSaving(user, groupId);
          if (groupSaving == null) {
            throw exceptionService.handleCreateBadRequest("Cannot fetch group transactions");
          }
          return getTransactions(groupSaving.id(), pageable);
        });
  }

  //Gets all of a users transactions in a group
  public Page<TransactionModel> getUserGroupTransactions(
      User user, String groupId, String customerId, Pageable pageable) {
    if (!customerProfileRepository.existsById(customerId)) {
      throw new BadRequestException("Customer profile does not exist");
    }

    GroupSavingModel groupSaving = groupSavingsService.getGroupSaving(user, groupId);
    if (groupSaving == null) {
      throw new BadRequestException("Invalid group information");
    }

    Page<Transaction> transactionPage =
        transactionRepository.findUserGroupTransactions(customerId, groupId, pageable);
    return transactionPage.map(Transaction::toModel);
  }

  private Page<TransactionModel> getTransactions(String entityId, Pageable pageable) {
    Page<Transaction> transactionsPage = transactionRepository.findAllForEntity(entityId, pageable);
    return transactionsPage.map(Transaction::toModel);
  }
}
