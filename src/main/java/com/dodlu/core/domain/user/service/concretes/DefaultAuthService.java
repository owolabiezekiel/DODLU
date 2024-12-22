package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.Constants.*;
import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.domain.user.enums.TokenType.RESET_PASSWORD;
import static com.dodlu.core.domain.user.enums.TokenType.VERIFY_PHONE;
import static com.dodlu.core.domain.user.model.EntityStatus.ACTIVE;
import static com.dodlu.core.domain.user.model.EntityStatus.INACTIVE;
import static com.dodlu.core.domain.user.model.UserType.*;
import static com.dodlu.core.domain.utils.Constants.VERIFY_PHONE_STRING;
import static com.dodlu.core.domain.utils.RoleUtils.getRoleByName;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.configuration.security.JwtService;
import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.exceptions.CustomBadCredentialsException;
import com.dodlu.core.app.model.request.notification.SmsRequest;
import com.dodlu.core.app.model.request.user.*;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.app.utils.StringUtilities;
import com.dodlu.core.app.utils.UserUtilities;
import com.dodlu.core.domain.notification.sms.SmsService;
import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.enums.Industry;
import com.dodlu.core.domain.user.model.UserModel;
import com.dodlu.core.domain.user.service.abstracts.AuthService;
import com.dodlu.core.persistence.auth.entity.Token;
import com.dodlu.core.persistence.auth.repository.TokenRepository;
import com.dodlu.core.persistence.company.entity.Company;
import com.dodlu.core.persistence.company.repository.CompanyRepository;
import com.dodlu.core.persistence.location.repository.CountryRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.RoleRepository;
import com.dodlu.core.persistence.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
  private final EmployerProfileRepository employerProfileRepository;
  private final CompanyRepository companyRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private final TokenRepository tokenRepository;
  private final AuthenticationManager authenticationManager;
  private final CountryRepository countryRepository;
  private final JwtService jwtService;
  private final SmsService smsService;
  private final DefaultUserService userService;
  private final CoreExceptionService exceptionService;

  @Override
  public CompletableFuture<UserModel> registerStudentUser(CreateUserRequest request) {
    return supplyAsync(
        () -> {
          checkUniqueFields(request);
          Role role = getRoleByName(roleRepository, exceptionService, STUDENT_ROLE_NAME_STRING);

          User user =
              buildUser(request.getUsername(), request.getEmail(), request.getPassword(), "", role);
          user.setStatus(INACTIVE).setType(STUDENT);
          user = userRepository.save(user);

          return userRepository.save(user).toModel();
        });
  }

  @Override
  public CompletableFuture<Void> registerCustomer(CreateCustomerRequest request) {
    return runAsync(
        () -> {
          doCreateCustomerValidation(request);

          Role role = getRoleByName(roleRepository, exceptionService, CUSTUMER_ROLE_NAME_STRING);
          var country = countryRepository.findByNameEqualsIgnoreCase(request.getCountry()).get();
          String phoneNumber = country.getPhoneExtensionCode() + request.getPhoneNumber();
          User user =
              buildUser(
                  request.getUsername(),
                  request.getEmail(),
                  request.getPassword(),
                  phoneNumber,
                  role);
          user.setStatus(ACTIVE)
              .setType(CUSTOMER); // to-do: set back to INACTIVE when we start phone validation
          userRepository.save(user);

          String passcode = UserUtilities.generateRandomDigits(4);

          var customerProfile =
              CustomerProfile.builder()
                  .username(user.getUsername())
                  .email(user.getEmail())
                  .phoneNumber(request.getPhoneNumber())
                  .firstName(request.getFirstName())
                  .lastName(request.getLastName())
                  .middleName(request.getMiddleName())
                  .gender(Gender.UNKNOWN)
                  .country(country)
                  .build();
          customerProfileRepository.save(customerProfile);

          Token token =
              Token.builder()
                  .user(user)
                  .code(passwordEncoder.encode(passcode))
                  .type(VERIFY_PHONE)
                  .createdAt(LocalDateTime.now())
                  .expiresAt(LocalDateTime.now().plusMinutes(30))
                  .build();

          tokenRepository.save(token);

          CompletableFuture.runAsync(
              () -> {
                var smsRequest =
                    SmsRequest.builder()
                        .message(
                            format(VERIFY_PHONE_STRING, customerProfile.getFirstName(), passcode))
                        .number(
                            country.getPhoneExtensionCode()
                                + StringUtilities.extractLastDigits(request.getPhoneNumber(), 10))
                        .build();
                smsService.send(smsRequest);
              });
        });
  }

  @Override
  @Transactional
  public CompletableFuture<UserModel> registerEmployer(RegisterEmployerRequest request) {
    return supplyAsync(
        () -> {
          doEmployerValidations(request);
          Role role = getRoleByName(roleRepository, exceptionService, EMPLOYER_ROLE_NAME_STRING);

          User user =
              buildUser(request.getEmail(), request.getEmail(), request.getPassword(), "", role);
          user.setStatus(ACTIVE).setType(EMPLOYER);
          user = userRepository.save(user);

          Company company =
              buildCompany(
                  request.getCompanyName(),
                  request.getIndustry(),
                  request.getVision(),
                  request.getMission());

          companyRepository.save(company);

          EmployerProfile employerProfile = buildEmployerProfile(user, request, company);
          employerProfileRepository.save(employerProfile);
          return user.toModel();
        });
  }

  @Override
  public CompletableFuture<LoginResponseModel> userLogin(
      UserLoginRequest request, HttpServletResponse response) {
    return supplyAsync(
        () -> {
          try {
            Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

            User user = (User) authentication.getPrincipal();
            if (user.getStatus().equals(INACTIVE) || user.isDeleted()) {
              throw exceptionService.handleCreateUnAuthorized("Account inactive or disabled");
            }
            String token = jwtService.generateToken(user);
            var profile = userService.getCurrentUserProfile(user).get();
            return LoginResponseModel.builder()
                .user(user.toModel())
                .accessToken(token)
                .profile(profile)
                .build();
          } catch (BadCredentialsException ex) {
            throw new CustomBadCredentialsException(
                "Invalid username / password combination", response);
          } catch (InternalAuthenticationServiceException ex) {
            throw new CustomBadCredentialsException("User does not exist", response);
          } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Override
  public CompletableFuture<Void> changePassword(User user, ChangePasswordRequest request) {
    return runAsync(
        () -> {
          log.info("{}", user.getPassword());
          if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw exceptionService.handleCreateBadRequest("Old password incorrect");
          }

          String newPasswordEncoded = passwordEncoder.encode(request.getNewPassword());
          user.setPassword(newPasswordEncoded);
          userRepository.save(user);
        });
  }

  @Override
  public CompletableFuture<Void> verifyPhone(VerifyPhoneRequest request) {
    return runAsync(
        () -> {
          CustomerProfile customerProfile =
              customerProfileRepository
                  .findByPhoneNumber(request.getPhoneNumber())
                  .orElseThrow(
                      () -> {
                        log.info(
                            "Customer with phone number {} does not exist",
                            request.getPhoneNumber());
                        return exceptionService.handleCreateBadRequest(
                            "Could not fetch customer details");
                      });

          User user =
              userRepository
                  .findByEmailEqualsIgnoreCase(customerProfile.getEmail())
                  .orElseThrow(
                      () -> exceptionService.handleCreateNotFoundException("user does not exist"));

          Token token =
              tokenRepository
                  .findByUserAndType(user, VERIFY_PHONE)
                  .orElseThrow(
                      () -> exceptionService.handleCreateBadRequest("Token does not exist"));

          if (!passwordEncoder.matches(request.getPasscode(), token.getCode())) {
            throw exceptionService.handleCreateBadRequest("Invalid passcode");
          }

          if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw exceptionService.handleCreateBadRequest("Passcode Expired");
          }

          user.setStatus(ACTIVE);
          userRepository.save(user);
          tokenRepository.delete(token);
        });
  }

  @Override
  public CompletableFuture<Void> requestPasswordReset(String email) {
    return runAsync(
        () -> {
          CustomerProfile customerProfile =
              customerProfileRepository
                  .findByEmailEqualsIgnoreCase(email)
                  .orElseThrow(
                      () -> {
                        log.info("Customer with email {} does not exist", email);
                        return exceptionService.handleCreateBadRequest("User does not exist");
                      });

          User user =
              userRepository
                  .findByEmailEqualsIgnoreCase(email)
                  .orElseThrow(
                      () -> {
                        log.info("User with email {} does not exist", email);
                        return exceptionService.handleCreateBadRequest("User does not exist");
                      });

          var existingToken = tokenRepository.findByUserAndType(user, RESET_PASSWORD);
          existingToken.ifPresent(tokenRepository::delete);

          String passcode = UserUtilities.generateRandomDigits(6);
          log.info("Passcode generated was {}", passcode);

          Token token =
              Token.builder()
                  .user(user)
                  .code(passwordEncoder.encode(passcode))
                  .type(RESET_PASSWORD)
                  .createdAt(LocalDateTime.now())
                  .expiresAt(LocalDateTime.now().plusDays(1))
                  .build();
          tokenRepository.save(token);

          CompletableFuture.runAsync(
              () -> {
                var smsRequest =
                    SmsRequest.builder()
                        .message(passcode)
                        .number(
                            customerProfile.getCountry().getPhoneExtensionCode()
                                + StringUtilities.extractLastDigits(
                                    customerProfile.getPhoneNumber(), 10))
                        .build();
                smsService.send(smsRequest);
              });
        });
  }

  @Override
  public CompletableFuture<Void> completePasswordReset(ResetPasswordRequest request) {
    return runAsync(
        () -> {
          User user =
              userRepository
                  .findByEmailEqualsIgnoreCase(request.getEmail())
                  .orElseThrow(
                      () -> {
                        log.info("User with email {} does not exist", request.getEmail());
                        return exceptionService.handleCreateBadRequest("User does not exist");
                      });

          var existingToken = tokenRepository.findByUserAndType(user, RESET_PASSWORD);
          if (existingToken.isEmpty()) {
            log.info("Token not found");
            throw exceptionService.handleCreateBadRequest("Invalid passcode supplied");
          }

          var token = existingToken.get();

          if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.info("Token has expired");
            throw exceptionService.handleCreateBadRequest("Invalid passcode supplied");
          }

          if (!passwordEncoder.matches(request.getPasscode(), token.getCode())) {
            log.info("Passcodes do not match");
            throw exceptionService.handleCreateBadRequest("Invalid passcode");
          }

          user.setPassword(passwordEncoder.encode(request.getNewPassword()));
          userRepository.save(user);

          tokenRepository.delete(token);
        });
  }

  private User buildUser(
      String username, String email, String password, String phoneNumber, Role role) {
    return User.builder()
        .username(isNullOrBlank(username) ? phoneNumber.strip() : username.toLowerCase().strip())
        .email(isNullOrBlank(email) ? null : email.strip())
        .password(passwordEncoder.encode(password.strip()))
        .phoneNumber(phoneNumber.strip())
        .roles(Set.of(role))
        .status(ACTIVE)
        .build();
  }

  private EmployerProfile buildEmployerProfile(
      User user, RegisterEmployerRequest request, Company company) {

    return EmployerProfile.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .phoneNumber(request.getPhoneNumber())
        .firstName(request.getFirstName().strip())
        .lastName(request.getLastName().strip())
        .company(company)
        .build();
  }

  private Company buildCompany(String name, Industry industry, String vision, String mission) {
    return Company.builder().name(name).industry(industry).vision(vision).mission(mission).build();
  }

  private void checkUniqueFields(CreateUserRequest request) {
    checkForDuplicateEmail(request.getEmail());
    checkForDuplicateUsername(request.getUsername());
  }

  private void doEmployerValidations(RegisterEmployerRequest request) {
    checkForDuplicateEmail(request.getEmail());
    checkForDuplicateUsername(request.getEmail());
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

  private void checkForDuplicatePhoneNumber(String phoneNumber, String countryName) {
    if (customerProfileRepository.existsByPhoneNumberAndCountry_NameEqualsIgnoreCase(
        phoneNumber, countryName)) {
      throw exceptionService.handleCreateBadRequest("Customer with phone number already exists");
    }
  }

  private void doCreateCustomerValidation(CreateCustomerRequest request) {
    if (!isNullOrBlank(request.getUsername())) {
      checkForDuplicateUsername(request.getUsername());
    }
    if (!isNullOrBlank(request.getEmail())) {
      checkForDuplicateEmail(request.getEmail());
    }
    validateCountry(request.getCountry());
    checkForDuplicatePhoneNumber(request.getPhoneNumber(), request.getCountry());
  }

  private void validateCountry(String country) {
    if (!countryRepository.existsByNameEqualsIgnoreCase(country)) {
      log.info("Country with name {} does not exist", country);
      throw exceptionService.handleCreateBadRequest(
          format("We currently do not support registration from %s", country));
    }
  }
}
