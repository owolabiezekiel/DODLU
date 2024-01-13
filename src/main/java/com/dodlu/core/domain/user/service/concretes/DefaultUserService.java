package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.Constants.*;
import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.app.utils.UserUtilities.generateRandomPassword;
import static com.dodlu.core.app.utils.UserUtilities.generateUsername;
import static com.dodlu.core.domain.user.model.EntityStatus.ACTIVE;
import static com.dodlu.core.domain.user.model.UserType.*;
import static com.dodlu.core.domain.utils.CoordinatorProfileUtils.getCoordinatorByUsername;
import static com.dodlu.core.domain.utils.DepartmentUtils.getDepartmentById;
import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.RoleUtils.getRoleByName;
import static com.dodlu.core.domain.utils.UserUtils.checkForDuplicateEmail;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.user.CreateCoordinatorRequest;
import com.dodlu.core.app.model.request.user.CreateStudentRequest;
import com.dodlu.core.app.model.request.user.CreateSupervisorRequest;
import com.dodlu.core.domain.user.enums.Level;
import com.dodlu.core.domain.user.model.CoordinatorProfileModel;
import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.persistence.department.entity.Department;
import com.dodlu.core.persistence.department.repository.DepartmentRepository;
import com.dodlu.core.persistence.internship.entity.Logbook;
import com.dodlu.core.persistence.internship.repository.LogbookRepository;
import com.dodlu.core.persistence.user.entity.*;
import com.dodlu.core.persistence.user.repository.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService {
  private final StudentProfileRepository studentProfileRepository;
  private final DepartmentRepository departmentRepository;
  private final SupervisorProfileRepository supervisorProfileRepository;
  private final CoordinatorProfileRepository coordinatorProfileRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final EmployerProfileRepository employerProfileRepository;
  private final LogbookRepository logbookRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<SupervisorProfileModel> createSupervisor(
      User user, CreateSupervisorRequest request) {
    return supplyAsync(
        () -> {
          doSuperVisorValidation(request);
          Role role = getRoleByName(roleRepository, exceptionService, SUPERVISOR_ROLE_NAME_STRING);
          EmployerProfile employer =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());

          String plainPassword = generateRandomPassword();
          User supervisor =
              buildUser(request.getUsername(), request.getEmail(), plainPassword, role);
          supervisor.setStatus(ACTIVE).setType(SUPERVISOR);
          supervisor = userRepository.save(supervisor);

          SupervisorProfile supervisorProfile =
              SupervisorProfile.builder()
                  .username(supervisor.getUsername())
                  .email(supervisor.getEmail())
                  .phoneNumber(request.getPhoneNumber())
                  .firstName(request.getFirstName())
                  .lastName(request.getLastName())
                  .gender(request.getGender())
                  .title(request.getTitle())
                  .employer(employer)
                  .build();
          log.info(
              "Will send email. Dear {} {} a supervisor account has been created for you with username {} and password {}",
              supervisorProfile.getFirstName(),
              supervisorProfile.getLastName(),
              supervisorProfile.getUsername(),
              plainPassword);
          return supervisorProfileRepository.save(supervisorProfile).toModel();
        });
  }

  @Transactional
  public CompletableFuture<CoordinatorProfileModel> createCoordinator(
      CreateCoordinatorRequest request) {
    return supplyAsync(
        () -> {
          doCoordinatorValidation(request);
          Role role = getRoleByName(roleRepository, exceptionService, COORDINATOR_ROLE_NAME_STRING);
          Department department =
              getDepartmentById(departmentRepository, exceptionService, request.getDepartmentId());

          String plainPassword = generateRandomPassword();
          User coordinator =
              buildUser(request.getUsername(), request.getEmail(), plainPassword, role);
          coordinator.setStatus(ACTIVE).setType(COORDINATOR);
          coordinator = userRepository.save(coordinator);

          department.setCoordinator(coordinator);
          departmentRepository.save(department);

          CoordinatorProfile coordinatorProfile =
              CoordinatorProfile.builder()
                  .username(coordinator.getUsername())
                  .email(coordinator.getEmail())
                  .phoneNumber(request.getPhoneNumber())
                  .firstName(request.getFirstName())
                  .lastName(request.getLastName())
                  .gender(request.getGender())
                  .title(request.getTitle())
                  .department(department)
                  .build();
          log.info(
              "Will send email. Dear {} {} a coordinator account has been created for you with username {} and password {}",
              coordinatorProfile.getFirstName(),
              coordinatorProfile.getLastName(),
              coordinatorProfile.getUsername(),
              plainPassword);
          return coordinatorProfileRepository.save(coordinatorProfile).toModel();
        });
  }

  @Transactional
  public CompletableFuture<StudentProfileModel> createStudent(
      User user, CreateStudentRequest request) {
    return supplyAsync(
        () -> {
          doStudentValidation(request);
          Role role = getRoleByName(roleRepository, exceptionService, STUDENT_ROLE_NAME_STRING);
          CoordinatorProfile coordinator =
              getCoordinatorByUsername(
                  coordinatorProfileRepository, exceptionService, user.getUsername());

          String plainPassword = generateRandomPassword();
          User student =
              buildUser(request.getMatricNumber(), request.getEmail(), plainPassword, role);
          student.setStatus(ACTIVE).setType(STUDENT);
          student = userRepository.save(student);

          StudentProfile studentProfile =
              StudentProfile.builder()
                  .username(student.getUsername())
                  .email(student.getEmail())
                  .phoneNumber(request.getPhoneNumber())
                  .firstName(request.getFirstName())
                  .lastName(request.getLastName())
                  .gender(request.getGender())
                  .programme(request.getProgramme())
                  .level(request.getLevel())
                  .department(coordinator.getDepartment())
                  .faculty(coordinator.getDepartment().getFaculty())
                  .build();
          log.info(
              "Will send email. Dear {} {} a student account has been created for you with username {} and password {}",
              studentProfile.getFirstName(),
              studentProfile.getLastName(),
              studentProfile.getUsername(),
              plainPassword);
          studentProfile = studentProfileRepository.save(studentProfile);

          Logbook logbook = buildLogbook(studentProfile);
          logbookRepository.save(logbook);

          return studentProfile.toModel();
        });
  }

  private User buildUser(String username, String email, String password, Role role) {
    return User.builder()
        .username(
            isNullOrBlank(username)
                ? generateUsername(email.toLowerCase().strip())
                : username.toLowerCase().strip())
        .email(email.strip())
        .password(passwordEncoder.encode(password.strip()))
        .roles(Set.of(role))
        .status(ACTIVE)
        .build();
  }

  private Logbook buildLogbook(StudentProfile studentProfile) {
    int logbookCapacity = calculateDurationFromLevel(studentProfile.getLevel());
    LocalDateTime validUntil = LocalDateTime.now().plusMonths(logbookCapacity);
    return Logbook.builder()
        .student(studentProfile)
        .capacityInMonths(logbookCapacity)
        .active(true)
        .validUntil(validUntil)
        .build();
  }

  private void doSuperVisorValidation(CreateSupervisorRequest request) {
    checkForDuplicateEmail(userRepository, exceptionService, request.getEmail());
  }

  private void doCoordinatorValidation(CreateCoordinatorRequest request) {
    checkForDuplicateEmail(userRepository, exceptionService, request.getEmail());
  }

  private void doStudentValidation(CreateStudentRequest request) {
    checkForDuplicateEmail(userRepository, exceptionService, request.getEmail());
  }

  private int calculateDurationFromLevel(Level level) {
    return switch (level) {
      case LEVEL_200 -> 2;
      case LEVEL_300 -> 3;
      case LEVEL_400 -> 6;
    };
  }
}
