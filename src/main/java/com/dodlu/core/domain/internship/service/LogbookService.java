package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.PlacementUtils.assertStudentHasPlacement;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static com.dodlu.core.domain.utils.SupervisorProfileUtils.getSupervisorProfileByUsername;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.internship.InsertLogbookEntryRequest;
import com.dodlu.core.app.model.request.internship.LogbookCommentRequest;
import com.dodlu.core.domain.internship.model.LogbookEntryModel;
import com.dodlu.core.domain.internship.model.LogbookModel;
import com.dodlu.core.persistence.internship.entity.Logbook;
import com.dodlu.core.persistence.internship.entity.LogbookEntry;
import com.dodlu.core.persistence.internship.repository.LogbookEntryRepository;
import com.dodlu.core.persistence.internship.repository.LogbookRepository;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.internship.repository.StudentSupervisorRepository;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.SupervisorProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import com.dodlu.core.persistence.user.repository.SupervisorProfileRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final SupervisorProfileRepository supervisorProfileRepository;
  private final EmployerProfileRepository employerProfileRepository;
  private final StudentSupervisorRepository studentSupervisorRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<LogbookModel> getLogbook(User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          log.info("{}", studentProfile);
          Logbook logbook =
              logbookRepository.findByStudentIdAndActiveIsTrue(studentProfile.getId()).orElse(null);
          log.info("{}", logbook);
          return logbook == null ? null : logbook.toModel();
        });
  }

  @Transactional
  public CompletableFuture<LogbookModel> getLogbookByStudentProfileId(String studentProfileId) {
    return supplyAsync(
        () -> {
          Logbook logbook =
              logbookRepository.findByStudentIdAndActiveIsTrue(studentProfileId).orElse(null);
          return logbook == null ? null : logbook.toModel();
        });
  }

  @Transactional
  public CompletableFuture<List<LogbookEntryModel>> getLogbookEntries(User user) {
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

          return logbookEntryRepository.findByLogbookId(logbook.getId()).stream()
              .map(LogbookEntry::toModel)
              .collect(Collectors.toList());
        });
  }

  @Transactional
  public CompletableFuture<List<LogbookEntryModel>> getLogbookEntriesByStudentProfileId(
      String studentProfileId) {
    return supplyAsync(
        () -> {
          Logbook logbook =
              logbookRepository
                  .findByStudentIdAndActiveIsTrue(studentProfileId)
                  .orElseThrow(
                      () -> exceptionService.handleCreateBadRequest("No Logbook found for user"));

          return logbookEntryRepository.findByLogbookId(logbook.getId()).stream()
              .map(LogbookEntry::toModel)
              .collect(Collectors.toList());
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
              .setDayOfWeek(today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
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

          if (logbook.getStartDate() != null) {
            throw exceptionService.handleCreateBadRequest(
                "Start date for this logbook has already been set");
          }

          LocalDate today = LocalDate.now();
          logbook.setStartDate(today);
          if (logbook.getCapacityInWeeks() == null || logbook.getCapacityInWeeks() <= 0) {
            int capacityInWeeks =
                logbook.getCapacityInMonths() < 6
                    ? (logbook.getCapacityInMonths() * 4) + 2
                    : (logbook.getCapacityInMonths() * 4) + 4;
            logbook.setCapacityInWeeks(capacityInWeeks);
            logbook.setValidUntil(LocalDateTime.now().plusWeeks(capacityInWeeks));
          }

          logbook = logbookRepository.save(logbook);

          if (!logbookEntryRepository.existsByLogbookId(logbook.getId())) {
            int numberOfWeeks = logbook.getCapacityInWeeks();
            LocalDate startMonday;
            if (today.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || today.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
              startMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            } else {
              startMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            }

            List<LogbookEntry> generatedLogbookEntries = new ArrayList<>();

            for (int i = 0; i < numberOfWeeks; i++) {
              LocalDate currentStartWeek = startMonday.plusWeeks(i);
              List<LogbookEntry> currentWeekEntry =
                  createLogbookEntryForWholeWeek(logbook, currentStartWeek, i + 1);
              generatedLogbookEntries.addAll(currentWeekEntry);
            }
            logbookEntryRepository.saveAll(generatedLogbookEntries);
          } else {
            log.info("Found existing logbook entries. Will not generate new logbook entries");
          }

          return logbook.toModel();
        });
  }

  @Transactional
  public CompletableFuture<Void> supervisorComment(
      User user, LogbookCommentRequest request, String studentProfileId) {
    return runAsync(
        () -> {
          SupervisorProfile supervisorProfile =
              getSupervisorProfileByUsername(
                  supervisorProfileRepository, exceptionService, user.getUsername());

          if (!studentSupervisorRepository.existsBySupervisorIdAndStudentIdAndActive(
              supervisorProfile.getId(), studentProfileId, false)) {
            throw exceptionService.handleCreateForbidden(
                "Cannot determine student-supervisor relationship");
          }

          int count =
              logbookEntryRepository.updateCommentForWeek(
                  request.getComment(), request.getWeekNumber());
          log.info("Updated {} records", count);
        });
  }

  @Transactional
  public CompletableFuture<Void> employerComment(
      User user, LogbookCommentRequest request, String studentProfileId) {
    return runAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());

          if (!placementRepository.existsByCompanyIdAndStudentId(
              employerProfile.getCompany().getId(), studentProfileId)) {
            throw exceptionService.handleCreateForbidden(
                "Student doesnt have placement in your organization");
          }

          int count =
              logbookEntryRepository.updateCommentForWeek(
                  request.getComment(), request.getWeekNumber());
          log.info("Updated {} records", count);
        });
  }

  private List<LogbookEntry> createLogbookEntryForWholeWeek(
      Logbook logbook, LocalDate startMonday, int weekNumber) {
    List<LogbookEntry> currentWeekEntry = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      LocalDate currentDay = startMonday.plusDays(i);
      LogbookEntry logbookEntry =
          LogbookEntry.builder()
              .logbook(logbook)
              .description(null)
              .approved(false)
              .weekNumber(weekNumber)
              .dayOfWeek(currentDay.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
              .dateTime(currentDay.atStartOfDay())
              .description("")
              .comment("")
              .build();

      currentWeekEntry.add(logbookEntry);
    }
    return currentWeekEntry;
  }
}
