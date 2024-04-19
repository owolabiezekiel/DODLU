package com.dodlu.core.domain.user.service.concretes;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.user.model.DashboardModel;
import com.dodlu.core.persistence.department.repository.DepartmentRepository;
import com.dodlu.core.persistence.faculty.repository.FacultyRepository;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.user.repository.CoordinatorProfileRepository;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
  private final DepartmentRepository departmentRepository;
  private final FacultyRepository facultyRepository;
  private final PlacementRepository placementRepository;
  private final StudentProfileRepository studentProfileRepository;
  private final EmployerProfileRepository employerRepository;
  private final CoordinatorProfileRepository coordinatorRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<DashboardModel> getOverview() {
    return supplyAsync(
        () -> {
          long departmentCount = departmentRepository.count();
          long facultyCount = facultyRepository.count();
          long activePlacementCount = placementRepository.countByActiveIsTrue();
          long totalPlacementCount = placementRepository.count();
          long totalActiveStudents = studentProfileRepository.countByIsDeletedIsFalse();
          long registeredEmployers = employerRepository.countByIsDeletedIsFalse();
          long totalCoordinators = coordinatorRepository.countByIsDeletedIsFalse();

          return DashboardModel.builder()
              .departmentCount(departmentCount)
              .facultyCount(facultyCount)
              .activePlacementCount(activePlacementCount)
              .totalPlacementCount(totalPlacementCount)
              .totalActiveStudents(totalActiveStudents)
              .registeredEmployers(registeredEmployers)
              .totalCoordinators(totalCoordinators)
              .build();
        });
  }
}
