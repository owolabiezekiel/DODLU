package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.internship.model.PlacementModel;
import com.dodlu.core.persistence.internship.entity.Placement;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPlacementService {
  private final PlacementRepository placementRepository;
  private final StudentProfileRepository studentProfileRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<PlacementModel> getPlacement(User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          Placement placement =
              placementRepository.findPlacementByStudentId(studentProfile.getId()).orElse(null);
          return placement == null ? null : placement.toModel();
        });
  }
}
