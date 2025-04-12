package com.dodlu.core.domain.banking.service;

import com.dodlu.core.app.model.request.banking.ActivateBankAccountRequest;
import com.dodlu.core.domain.banking.model.BankAccountDetailsModel;
import com.dodlu.core.persistence.user.entity.User;

public interface BankingService {
  BankAccountDetailsModel activateBankAccountForCustomer(
      User user, ActivateBankAccountRequest request);
}
