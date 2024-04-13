package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileById;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static com.dodlu.core.domain.utils.SupervisorProfileUtils.getSupervisorProfileById;
import static com.dodlu.core.domain.utils.SupervisorProfileUtils.getSupervisorProfileByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.internship.AssignSupervisorRequest;
import com.dodlu.core.domain.internship.model.PlacementModel;
import com.dodlu.core.domain.internship.model.StudentSupervisorModel;
import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.persistence.internship.entity.Placement;
import com.dodlu.core.persistence.internship.entity.StudentSupervisor;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.internship.repository.StudentSupervisorRepository;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.SupervisorProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import com.dodlu.core.persistence.user.repository.SupervisorProfileRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPlacementService {
  private final EmployerProfileRepository employerProfileRepository;
  private final PlacementRepository placementRepository;
  private final StudentProfileRepository studentProfileRepository;
  private final SupervisorProfileRepository supervisorProfileRepository;
  private final StudentSupervisorRepository studentSupervisorRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<PlacementModel> getPlacement(User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          Placement placement =
              placementRepository
                  .findPlacementByStudentIdAndActiveIsTrue(studentProfile.getId())
                  .orElse(null);
          return placement == null ? null : placement.toModel();
        });
  }

  @Transactional
  public CompletableFuture<StudentSupervisorModel> assignStudentSupervisor(
      User user, AssignSupervisorRequest request) {
    return supplyAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());
          checkStudentHasPlacementInCompany(
              employerProfile.getCompany().getId(), request.getStudentId());
          checkNoSupervisorAssigned(request, employerProfile);

          StudentProfile studentProfile =
              getStudentProfileById(
                  studentProfileRepository, exceptionService, request.getStudentId());
          SupervisorProfile supervisorProfile =
              getSupervisorProfileById(
                  supervisorProfileRepository, exceptionService, request.getSupervisorId());

          StudentSupervisor studentSupervisor =
              StudentSupervisor.builder()
                  .student(studentProfile)
                  .supervisor(supervisorProfile)
                  .company(employerProfile.getCompany())
                  .active(true)
                  .build();
          return studentSupervisorRepository.save(studentSupervisor).toModel();
        });
  }

  @Transactional
  public CompletableFuture<SupervisorProfileModel> getStudentSupervisor(User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          checkStudentHasActivePlacement(studentProfile.getId());
          StudentSupervisor studentSupervisor =
              studentSupervisorRepository
                  .findByStudentIdAndActiveIsTrue(studentProfile.getId())
                  .orElse(null);
          return studentSupervisor == null ? null : studentSupervisor.getSupervisor().toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<StudentProfileModel>> getSupervisorStudents(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          SupervisorProfile supervisorProfile =
              getSupervisorProfileByUsername(
                  supervisorProfileRepository, exceptionService, user.getUsername());

          Page<StudentSupervisor> pagedContent =
              studentSupervisorRepository.findStudentSupervisorBySupervisorIdAndActiveIsTrue(
                  pageable, supervisorProfile.getId());
          List<StudentProfileModel> studentProfiles =
              pagedContent.stream()
                  .map(studentSupervisor -> studentSupervisor.getStudent().toModel())
                  .toList();
          return new PageImpl<>(
              studentProfiles, pagedContent.getPageable(), pagedContent.getTotalElements());
        });
  }

  @Transactional
  public CompletableFuture<Page<StudentProfileModel>> getInterns(User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          EmployerProfile employer =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());

          Page<Placement> pagedContent =
              placementRepository.findAllByCompanyId(employer.getCompany().getId(), pageable);
          List<StudentProfileModel> studentProfiles =
              pagedContent.stream().map(placement -> placement.getStudent().toModel()).toList();
          return new PageImpl<>(
              studentProfiles, pagedContent.getPageable(), pagedContent.getTotalElements());
        });
  }

  private void checkNoSupervisorAssigned(
      AssignSupervisorRequest request, EmployerProfile employer) {
    if (studentSupervisorRepository.existsByCompanyIdAndStudentIdAndActive(
        employer.getCompany().getId(), request.getStudentId(), true)) {
      throw exceptionService.handleCreateBadRequest(
          "Supervisor has already been assigned to this student");
    }
  }

  private void checkStudentHasPlacementInCompany(String companyId, String studentId) {
    if (!placementRepository.existsByCompanyIdAndStudentId(companyId, studentId)) {
      throw exceptionService.handleCreateBadRequest(
          "Student does not have a placement in your company");
    }
  }

  private void checkStudentHasActivePlacement(String studentId) {
    if (!placementRepository.existsByStudentIdAndActive(studentId, true)) {
      throw exceptionService.handleCreateBadRequest("You are yet to secure a placement");
    }
  }
}
