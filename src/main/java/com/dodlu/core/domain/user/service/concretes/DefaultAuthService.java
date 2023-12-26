package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.Constants.EMPLOYER_ROLE_NAME_STRING;
import static com.dodlu.core.app.utils.Constants.STUDENT_ROLE_NAME_STRING;
import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.app.utils.UserUtilities.generateUsername;
import static com.dodlu.core.domain.user.model.EntityStatus.ACTIVE;
import static com.dodlu.core.domain.user.model.EntityStatus.INACTIVE;
import static com.dodlu.core.domain.user.model.UserType.EMPLOYER;
import static com.dodlu.core.domain.user.model.UserType.STUDENT;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.configuration.security.JwtService;
import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.user.CreateEmployerRequest;
import com.dodlu.core.app.model.request.user.CreateUserRequest;
import com.dodlu.core.app.model.request.user.UserLoginRequest;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.UserModel;
import com.dodlu.core.domain.user.service.abstracts.AuthService;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.RoleRepository;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultAuthService implements AuthService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final EmployerProfileRepository employerProfileRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final CoreExceptionService exceptionService;

  @Override
  public CompletableFuture<UserModel> registerStudentUser(CreateUserRequest request) {
    return supplyAsync(
        () -> {
          checkUniqueFields(request);
          Role role = getRole(STUDENT_ROLE_NAME_STRING);

          User user =
              buildUser(request.getUsername(), request.getEmail(), request.getPassword(), role);
          user.setStatus(INACTIVE).setType(STUDENT);
          user = userRepository.save(user);

          return userRepository.save(user).toModel();
        });
  }

  @Override
  @Transactional
  public CompletableFuture<UserModel> registerEmployer(CreateEmployerRequest request) {
    return supplyAsync(
        () -> {
          doEmployerValidations(request);
          Role role = getRole(EMPLOYER_ROLE_NAME_STRING);

          User user =
              buildUser(request.getUsername(), request.getEmail(), request.getPassword(), role);
          user.setStatus(ACTIVE).setType(EMPLOYER);
          user = userRepository.save(user);

          EmployerProfile employerProfile = buildEmployerProfile(user, request);
          employerProfileRepository.save(employerProfile);
          return user.toModel();
        });
  }

  @Override
  public CompletableFuture<LoginResponseModel> userLogin(UserLoginRequest request) {
    return supplyAsync(
        () -> {
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  request.getUsername(), request.getPassword()));
          User user =
              userRepository
                  .findByUsernameEqualsIgnoreCase(request.getUsername())
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateUnAuthorized(
                              "Invalid username/password combination"));
          String token = jwtService.generateToken(user);
          return LoginResponseModel.builder().user(user.toModel()).accessToken(token).build();
        });
  }

  private Role getRole(String name) {
    return roleRepository
        .findByNameEqualsIgnoreCase(name)
        .orElseThrow(
            () -> exceptionService.handleCreateBadRequest(format("Could not get role %s", name)));
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

  private EmployerProfile buildEmployerProfile(User user, CreateEmployerRequest request) {
    return EmployerProfile.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .phoneNumber(request.getPhoneNumber())
        .firstName(request.getFirstName().strip())
        .lastName(request.getLastName().strip())
        .companyName(request.getCompanyName().strip())
        .vision(request.getVision())
        .mission(request.getMission())
        .industry(request.getIndustry())
        .build();
  }

  private void checkUniqueFields(CreateUserRequest request) {
    checkForDuplicateEmail(request.getEmail());
    checkForDuplicateUsername(request.getUsername());
  }

  private void doEmployerValidations(CreateEmployerRequest request) {
    checkForDuplicateEmail(request.getEmail());
    checkForDuplicateUsername(request.getUsername());
  }

  private void checkForDuplicateEmail(String email) {
    if (userRepository.existsByEmailEqualsIgnoreCase(email.strip())) {
      throw exceptionService.handleCreateBadRequest("A user with this email already exists");
    }
  }

  private void checkForDuplicateUsername(String username) {
    if (userRepository.existsByUsernameEqualsIgnoreCase(username.strip())) {
      throw exceptionService.handleCreateBadRequest("This username is already taken");
    }
  }
}
