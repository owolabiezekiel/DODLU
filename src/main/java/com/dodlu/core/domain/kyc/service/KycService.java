package com.dodlu.core.domain.kyc.service;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.User;
import java.util.concurrent.CompletableFuture;

public interface KycService {
  default CompletableFuture<Void> verifyBVN(User user, String bvn) {
    throw new CoreExceptionService().handleCreateNotImplementedException("Not Implemented");
  }

  default CompletableFuture<Void> verifyNIN() {
    throw new CoreExceptionService().handleCreateNotImplementedException("Not Implemented");
  }
}
