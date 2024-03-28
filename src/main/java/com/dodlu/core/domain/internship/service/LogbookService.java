package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.domain.utils.PlacementUtils.assertStudentHasPlacement;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.internship.InsertLogbookEntryRequest;
import com.dodlu.core.domain.internship.model.LogbookEntryModel;
import com.dodlu.core.domain.internship.model.LogbookModel;
import com.dodlu.core.persistence.internship.entity.Logbook;
import com.dodlu.core.persistence.internship.entity.LogbookEntry;
import com.dodlu.core.persistence.internship.repository.LogbookEntryRepository;
import com.dodlu.core.persistence.internship.repository.LogbookRepository;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogbookService {
  private final StudentProfileRepository studentProfileRepository;
  private final LogbookRepository logbookRepository;
  private final LogbookEntryRepository logbookEntryRepository;
  private final PlacementRepository placementRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<LogbookModel> getLogbook(User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          Logbook logbook =
              logbookRepository.findByStudentIdAndActiveIsTrue(studentProfile.getId()).orElse(null);
          return logbook == null ? null : logbook.toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<LogbookEntryModel>> getLogbookEntries(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          Logbook logbook =
              logbookRepository
                  .findByStudentIdAndActiveIsTrue(studentProfile.getId())
                  .orElseThrow(
                      () -> exceptionService.handleCreateBadRequest("No Logbook found for user"));

          return logbookEntryRepository
              .findByLogbookId(pageable, logbook.getId())
              .map(LogbookEntry::toModel);
        });
  }

  @Transactional
  public CompletableFuture<LogbookEntryModel> insertLogbookEntry(
      User user, InsertLogbookEntryRequest request) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());
          assertStudentHasPlacement(placementRepository, exceptionService, studentProfile.getId());

          Logbook logbook =
              logbookRepository
                  .findByStudentIdAndActiveIsTrue(studentProfile.getId())
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateBadRequest("No Logbook found for student"));

          LocalDateTime today = LocalDateTime.now();
          LogbookEntry logbookEntry =
              logbookEntryRepository
                  .findByLogbookAndDate(logbook, today)
                  .orElse(new LogbookEntry());
          logbookEntry
              .setLogbook(logbook)
              .setDescription(request.getDescription())
              .setApproved(false)
              .setDateTime(today)
              .setComment("");

          return logbookEntryRepository.save(logbookEntry).toModel();
        });
  }

  @Transactional
  public CompletableFuture<LogbookModel> setLogbookStartDate(@AuthenticationPrincipal User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());
          assertStudentHasPlacement(placementRepository, exceptionService, studentProfile.getId());

          Logbook logbook =
              logbookRepository
                  .findByStudentIdAndActiveIsTrue(studentProfile.getId())
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateBadRequest("No Logbook found for student"));

          LocalDate today = LocalDate.now();
          logbook.setStartDate(today);
          return logbookRepository.save(logbook).toModel();
        });
  }
}
