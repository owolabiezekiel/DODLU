package com.dodlu.core.domain.savings.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.savings.CreateGroupSavingRequest;
import com.dodlu.core.domain.savings.model.GroupSavingModel;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.savings.entity.GroupSavingUser;
import com.dodlu.core.persistence.savings.repository.GroupSavingRepository;
import com.dodlu.core.persistence.savings.repository.GroupSavingUsersRepository;
import com.dodlu.core.persistence.transaction.entity.GroupBalance;
import com.dodlu.core.persistence.transaction.repository.GroupBalanceRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupSavingsService {
  private final GroupSavingRepository groupSavingRepository;
  private final GroupSavingUsersRepository groupSavingUsersRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private final CoreExceptionService exceptionService;
  private final GroupBalanceRepository groupBalanceRepository;

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

          groupSaving.setMembers(Set.of(customerProfile));
          groupSaving.setBalance(groupBalance);
          return groupSaving.toModel();
        });
  }

  private GroupBalance generateEmptyGroupBalance(GroupSaving groupSaving) {
    return GroupBalance.builder()
        .groupSavings(groupSaving)
        .availableBalance(BigDecimal.ZERO)
        .build();
  }

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
}
