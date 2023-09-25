package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.Constants.STUDENT_ROLE_NAME_STRING;
import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.app.utils.UserUtilities.generateUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.user.CreateUserRequest;
import com.dodlu.core.app.model.request.user.UserLoginRequest;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.auth.abstracts.JwtService;
import com.dodlu.core.domain.user.model.AppUserModel;
import com.dodlu.core.domain.user.model.EntityStatus;
import com.dodlu.core.domain.user.model.UserType;
import com.dodlu.core.domain.user.service.abstracts.AuthService;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.RoleRepository;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultAuthService implements AuthService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final CoreExceptionService exceptionService;

  @Override
  public CompletableFuture<AppUserModel> registerStudentUser(CreateUserRequest request) {
    return supplyAsync(
        () -> {
          checkUniqueFields(request);
          Role role = getRole(STUDENT_ROLE_NAME_STRING);

          User user =
              User.builder()
                  .username(
                      isNullOrBlank(request.getUsername())
                          ? generateUsername(request.getEmail()).toLowerCase().strip()
                          : request.getUsername().toLowerCase().strip())
                  .email(request.getEmail().strip())
                  .password(passwordEncoder.encode(request.getPassword().strip()))
                  .type(UserType.STUDENT)
                  .roles(Set.of(role))
                  .status(EntityStatus.INACTIVE)
                  .build();

          return userRepository.save(user).toModel();
        });
  }

  @Override
  public CompletableFuture<LoginResponseModel> userLogin(UserLoginRequest request) {
    return supplyAsync(
        () -> {
          try {
            Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));
            String token = jwtService.generate(authentication).join();
            return LoginResponseModel.builder()
                .user(
                    userRepository
                        .findByUsernameEqualsIgnoreCase(request.getUsername())
                        .get()
                        .toModel())
                .jwt(token)
                .build();
          } catch (BadCredentialsException e) {
            throw exceptionService.handleCreateBadRequest("Invalid username/password combination");
          }
        });
  }

  private Role getRole(String name) {
    return roleRepository
        .findByNameEqualsIgnoreCase(name)
        .orElseThrow(() -> new RuntimeException("Could not get role"));
  }

  private void checkUniqueFields(CreateUserRequest request) {
    if (userRepository.existsByEmailEqualsIgnoreCase(request.getEmail().strip())) {
      throw exceptionService.handleCreateBadRequest("A user with this email already exists");
    }
    if (userRepository.existsByUsernameEqualsIgnoreCase(request.getUsername().strip())) {
      throw exceptionService.handleCreateBadRequest("This username is already taken");
    }
  }
}
