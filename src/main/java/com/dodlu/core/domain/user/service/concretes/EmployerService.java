package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.SupervisorProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.SupervisorProfileRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployerService {
  private final SupervisorProfileRepository supervisorProfileRepository;
  private final EmployerProfileRepository employerProfileRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<Page<SupervisorProfileModel>> getEmployerSupervisors(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());

          return supervisorProfileRepository
              .findAllByEmployerId(pageable, employerProfile.getId())
              .map(SupervisorProfile::toModel);
        });
  }
}
