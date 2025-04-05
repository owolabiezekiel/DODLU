package com.dodlu.core.domain.banking.service;

import com.dodlu.core.app.exceptions.BadRequestException;
import com.dodlu.core.app.model.request.banking.ActivateBankAccountRequest;
import com.dodlu.core.domain.banking.model.BankAccountDetailsModel;
import com.dodlu.core.persistence.systemsettings.repository.SystemSettingsRepository;
import com.dodlu.core.persistence.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankingServiceImpl implements BankingService {
  private final SystemSettingsRepository systemSettingsRepository;

  @Override
  public BankAccountDetailsModel activateBankAccount(
      User user, ActivateBankAccountRequest request) {
    checkActivationStatus();
    // ensureNoWalletExistsForUser(user.getUsername());
    //
    //    HttpHeaders headers = setupRequestHeaders();
    //
    //    IndividualCustomerResponse individualCustomerResponse =
    //        createIndividualCustomer(headers, user, activateWalletRequest);
    //
    //    CreateSubAccountResponse subAccountResponse =
    //        createSubAccountForCustomer(headers, user, individualCustomerResponse);
    //
    //    String fullName = buildFullName(activateWalletRequest);
    //
    //    VirtualNubanDetailsResponse virtualNubanDetails =
    //        createVirtualAccountForCustomer(headers, user, subAccountResponse, fullName);
    //
    //    WalletDetails walletDetails =
    //        new WalletDetails()
    //            .fromSubAccountResponse(subAccountResponse, user, virtualNubanDetails);
    //    walletDetails.setPin(passwordEncoder.encode(activateWalletRequest.getPin()));
    //    walletDetails.setDailyLimit(DAILY_LIMIT);
    //
    //    walletDetailsRepository.save(walletDetails);
    //
    //    AnchorIndividualCustomer individualCustomer =
    //        anchorIndividualCustomerRepository.findByUsernameIgnoreCase(user.getUsername());
    //    individualCustomer.setCompleted(true);
    //    anchorIndividualCustomerRepository.save(individualCustomer);
    //
    //    return walletDetails.toModel();
    return null;
  }

  private void checkActivationStatus() {
    var systemSettings = systemSettingsRepository.findFirstByOrderByIdDesc();
    if (systemSettings.isEmpty() || !systemSettings.get().getBankAccountActivationEnabled()) {
      throw new BadRequestException(
          "Bank account activation is not enabled on at the moment. Please try again later");
    }
  }
}
