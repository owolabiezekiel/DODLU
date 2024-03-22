package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.Constants.*;
import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.app.utils.UserUtilities.generateRandomPassword;
import static com.dodlu.core.app.utils.UserUtilities.generateUsername;
import static com.dodlu.core.domain.user.enums.Gender.*;
import static com.dodlu.core.domain.user.enums.Level.*;
import static com.dodlu.core.domain.user.enums.Programme.POSTGRADUATE;
import static com.dodlu.core.domain.user.enums.Programme.UNDERGRADUATE;
import static com.dodlu.core.domain.user.model.EntityStatus.ACTIVE;
import static com.dodlu.core.domain.user.model.UserType.*;
import static com.dodlu.core.domain.utils.CoordinatorProfileUtils.getCoordinatorByUsername;
import static com.dodlu.core.domain.utils.DepartmentUtils.getDepartmentById;
import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.RoleUtils.getRoleByName;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static com.dodlu.core.domain.utils.SupervisorProfileUtils.getSupervisorProfileByUsername;
import static com.dodlu.core.domain.utils.UserUtils.*;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.file.ImageDownloadRequest;
import com.dodlu.core.app.model.request.user.CreateCoordinatorRequest;
import com.dodlu.core.app.model.request.user.CreateStudentRequest;
import com.dodlu.core.app.model.request.user.CreateSupervisorRequest;
import com.dodlu.core.app.model.request.user.UpdateStudentProfileRequest;
import com.dodlu.core.domain.file.service.abstracts.StorageService;
import com.dodlu.core.domain.internship.model.StudentDocumentModel;
import com.dodlu.core.domain.notification.email.service.concretes.JmsEmailService;
import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.enums.Level;
import com.dodlu.core.domain.user.enums.Programme;
import com.dodlu.core.domain.user.model.CoordinatorProfileModel;
import com.dodlu.core.domain.user.model.GeneralProfileModel;
import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.persistence.base.UserProfile;
import com.dodlu.core.persistence.department.entity.Department;
import com.dodlu.core.persistence.department.repository.DepartmentRepository;
import com.dodlu.core.persistence.internship.entity.Logbook;
import com.dodlu.core.persistence.internship.entity.StudentDocuments;
import com.dodlu.core.persistence.internship.repository.LogbookRepository;
import com.dodlu.core.persistence.internship.repository.StudentDocumentRepository;
import com.dodlu.core.persistence.user.entity.*;
import com.dodlu.core.persistence.user.repository.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService {
  private final StudentProfileRepository studentProfileRepository;
  private final DepartmentRepository departmentRepository;
  private final SupervisorProfileRepository supervisorProfileRepository;
  private final CoordinatorProfileRepository coordinatorProfileRepository;
  private final StudentDocumentRepository studentDocumentRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final EmployerProfileRepository employerProfileRepository;
  private final LogbookRepository logbookRepository;
  private final StorageService storageService;
  private final JmsEmailService emailService;
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

          supervisorProfile = supervisorProfileRepository.save(supervisorProfile);
          emailService.sendNewAccountEmail(
              supervisor.getEmail(), plainPassword, supervisor.getUsername(), SUPERVISOR);
          return supervisorProfile.toModel();
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

          coordinatorProfile = coordinatorProfileRepository.save(coordinatorProfile);
          emailService.sendNewAccountEmail(
              coordinator.getEmail(), plainPassword, coordinator.getUsername(), COORDINATOR);
          return coordinatorProfile.toModel();
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
                  .cgpa(request.getCgpa())
                  .build();

          studentProfile = studentProfileRepository.save(studentProfile);

          Logbook logbook = buildLogbook(studentProfile);
          logbookRepository.save(logbook);

          emailService.sendNewAccountEmail(
              student.getEmail(), plainPassword, student.getUsername(), STUDENT);
          return studentProfile.toModel();
        });
  }

  @Transactional
  public CompletableFuture<Void> createStudents(User user, MultipartFile file) {
    return runAsync(
        () -> {
          String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
          if (filenameExtension == null
              || (!filenameExtension.equalsIgnoreCase("xlsx")
                  && !filenameExtension.equalsIgnoreCase("xls"))) {
            throw exceptionService.handleCreateBadRequest("Unsupported file format");
          }

          Role role = getRoleByName(roleRepository, exceptionService, STUDENT_ROLE_NAME_STRING);
          CoordinatorProfile coordinator =
              getCoordinatorByUsername(
                  coordinatorProfileRepository, exceptionService, user.getUsername());

          List<String> errorMessages = new ArrayList<>();

          try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            Row row;

            for (int i = 1; i <= lastRowNum; i++) {
              row = sheet.getRow(i);
              String matricNumber = row.getCell(0).getStringCellValue();
              if (userRepository.existsByUsernameEqualsIgnoreCase(matricNumber)) {
                User studentUser = getUser(userRepository, exceptionService, matricNumber);
                if (studentUser.isDeleted()) {
                  studentUser.setDeleted(false);

                  StudentProfile studentProfile =
                      getStudentProfileByUsername(
                          studentProfileRepository, exceptionService, matricNumber);
                  studentProfile.setDeleted(false);
                  int level = (int) row.getCell(1).getNumericCellValue();
                  studentProfile.setLevel(getLevelFromNumber(level));
                  studentProfileRepository.save(studentProfile);

                  Logbook logbook = buildLogbook(studentProfile);
                  logbookRepository.save(logbook);
                } else {
                  errorMessages.add(
                      format(
                          "%s currently has an active profile. consider deactivating them first",
                          matricNumber));
                }
              } else {
                int level = (int) row.getCell(1).getNumericCellValue();
                String firstName = row.getCell(2).getStringCellValue();
                String lastName = row.getCell(3).getStringCellValue();
                String email = row.getCell(4).getStringCellValue();
                String phoneNumber = row.getCell(5).getStringCellValue();
                String gender = row.getCell(6).getStringCellValue();
                String programme = row.getCell(7).getStringCellValue();
                Double cgpa = row.getCell(8).getNumericCellValue();
                String plainPassword = generateRandomPassword();

                User student = buildUser(matricNumber, email, plainPassword, role);
                student.setStatus(ACTIVE).setType(STUDENT);
                userRepository.save(student);

                StudentProfile studentProfile =
                    StudentProfile.builder()
                        .username(matricNumber)
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .firstName(firstName)
                        .lastName(lastName)
                        .gender(getGenderFromString(gender))
                        .programme(getProgrammeFromString(programme))
                        .level(getLevelFromNumber(level))
                        .department(coordinator.getDepartment())
                        .cgpa(cgpa)
                        .build();
                studentProfile = studentProfileRepository.save(studentProfile);

                Logbook logbook = buildLogbook(studentProfile);
                logbookRepository.save(logbook);
                emailService.sendNewAccountEmail(
                    student.getEmail(), plainPassword, student.getUsername(), STUDENT);
              }
            }

            if (!errorMessages.isEmpty()) {
              throw exceptionService.handleCreateException(
                  "Found some errors", CREATED, errorMessages);
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Transactional
  public CompletableFuture<? extends UserProfile> updateDisplayPicture(
      User user, MultipartFile file) {
    return supplyAsync(
        () -> {
          switch (user.getType()) {
            case STUDENT -> {
              StudentProfile studentProfile =
                  getStudentProfileByUsername(
                      studentProfileRepository, exceptionService, user.getUsername());
              return updateProfileImage(studentProfile, studentProfileRepository, file);
            }
            case EMPLOYER -> {
              EmployerProfile employerProfile =
                  getEmployerByUsername(
                      employerProfileRepository, exceptionService, user.getUsername());
              return updateProfileImage(employerProfile, employerProfileRepository, file);
            }
            default -> throw exceptionService.handleCreateBadRequest("Unknown user type");
          }
        });
  }

  @Transactional
  public CompletableFuture<StudentDocumentModel> studentUpdateResume(
      User user, MultipartFile file) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          StudentDocuments studentDocuments = getStudentDocuments(studentProfile);

          if (!isNullOrBlank(studentDocuments.getResumeUrl())) {
            ImageDownloadRequest request =
                new ImageDownloadRequest().setImageUrl(studentDocuments.getResumeUrl());
            storageService.deleteFile(request);
          }
          String newResumeUrl = storageService.uploadImage(file).join();
          studentDocuments.setResumeUrl(newResumeUrl);
          return studentDocumentRepository.save(studentDocuments).toModel();
        });
  }

  @Transactional
  public CompletableFuture<StudentDocumentModel> studentUpdateCoverLetter(
      User user, MultipartFile file) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          StudentDocuments studentDocuments = getStudentDocuments(studentProfile);

          if (!isNullOrBlank(studentDocuments.getCoverLetterUrl())) {
            ImageDownloadRequest request =
                new ImageDownloadRequest().setImageUrl(studentDocuments.getCoverLetterUrl());
            storageService.deleteFile(request);
          }
          String newCoverLetterUrl = storageService.uploadImage(file).join();
          studentDocuments.setCoverLetterUrl(newCoverLetterUrl);
          return studentDocumentRepository.save(studentDocuments).toModel();
        });
  }

  @Transactional
  public CompletableFuture<StudentDocumentModel> studentUpdateTranscript(
      User user, MultipartFile file) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          StudentDocuments studentDocuments = getStudentDocuments(studentProfile);

          if (!isNullOrBlank(studentDocuments.getTranscriptUrl())) {
            ImageDownloadRequest request =
                new ImageDownloadRequest().setImageUrl(studentDocuments.getTranscriptUrl());
            storageService.deleteFile(request);
          }
          String newTranscriptUrl = storageService.uploadImage(file).join();
          studentDocuments.setTranscriptUrl(newTranscriptUrl);
          return studentDocumentRepository.save(studentDocuments).toModel();
        });
  }

  @Transactional
  public CompletableFuture<GeneralProfileModel> getCurrentUserProfile(User user) {
    return supplyAsync(
        () -> {
          switch (user.getType()) {
            case STUDENT -> {
              return getStudentProfileByUsername(
                      studentProfileRepository, exceptionService, user.getUsername())
                  .toGeneralProfileModel();
            }
            case EMPLOYER -> {
              return getEmployerByUsername(
                      employerProfileRepository, exceptionService, user.getUsername())
                  .toGeneralProfileModel();
            }
            case SUPERVISOR -> {
              return getSupervisorProfileByUsername(
                      supervisorProfileRepository, exceptionService, user.getUsername())
                  .toGeneralProfileModel();
            }
            case COORDINATOR -> {
              return getCoordinatorByUsername(
                      coordinatorProfileRepository, exceptionService, user.getUsername())
                  .toGeneralProfileModel();
            }
            default -> throw exceptionService.handleCreateBadRequest("Unknown user type");
          }
        });
  }

  //  @Transactional
  //  public CompletableFuture<Void> sendSampleEmail() {
  //    return runAsync(
  //        () ->
  //            emailService.sendNewAccountEmail(
  //                "owo.ezekiel@gmail.com", "some-pass", "some-username", STUDENT));
  //  }

  @Transactional
  public CompletableFuture<StudentDocumentModel> getStudentDocuments(User user) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          return getStudentDocuments(studentProfile).toModel();
        });
  }

  @Transactional
  public CompletableFuture<StudentProfileModel> updateStudentProfile(
      User user, UpdateStudentProfileRequest request) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          if (!isNullOrBlank(request.getPhoneNumber()))
            studentProfile.setPhoneNumber(request.getPhoneNumber());
          if (!isNullOrBlank(request.getEmail())) {
            studentProfile.setEmail(request.getEmail());
            user.setEmail(request.getEmail());
            userRepository.save(user);
          }
          return studentProfileRepository.save(studentProfile).toModel();
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

  private Level getLevelFromNumber(int level) {
    return switch (level) {
      case 200 -> LEVEL_200;
      case 300 -> LEVEL_300;
      case 400 -> LEVEL_400;
      default -> throw exceptionService.handleCreateBadRequest("Unknown level");
    };
  }

  private Gender getGenderFromString(String gender) {
    return switch (gender.toLowerCase()) {
      case "male" -> MALE;
      case "female" -> FEMALE;
      default -> OTHERS;
    };
  }

  private Programme getProgrammeFromString(String programme) {
    return programme.equalsIgnoreCase("postgraduate") ? POSTGRADUATE : UNDERGRADUATE;
  }

  private <T extends UserProfile, R extends JpaRepository<T, ?>> T updateProfileImage(
      T userProfile, R repository, MultipartFile file) {
    try {
      if (!isNullOrBlank(userProfile.getProfileImageUrl())) {
        ImageDownloadRequest request =
            new ImageDownloadRequest().setImageUrl(userProfile.getProfileImageUrl());
        storageService.deleteFile(request).join();
      }
      String newImageUrl = storageService.uploadImage(file).join();
      userProfile.setProfileImageUrl(newImageUrl);
      return repository.save(userProfile);
    } catch (Exception e) {
      throw exceptionService.handleCreateException(e.getMessage(), INTERNAL_SERVER_ERROR);
    }
  }

  private StudentDocuments getStudentDocuments(StudentProfile studentProfile) {
    StudentDocuments studentDocuments =
        studentDocumentRepository.findByStudent(studentProfile).orElse(null);
    if (studentDocuments == null) {
      studentDocuments = StudentDocuments.builder().student(studentProfile).build();
      studentDocuments = studentDocumentRepository.save(studentDocuments);
    }
    return studentDocuments;
  }
}
