package com.dodlu.core.domain.immunization.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.immunization.CreateChildRequest;
import com.dodlu.core.app.model.request.immunization.CreateParentRequest;
import com.dodlu.core.app.model.request.immunization.SearchAttributes;
import com.dodlu.core.domain.immunization.model.ChildWithParentModel;
import com.dodlu.core.domain.immunization.model.ParentModel;
import com.dodlu.core.domain.immunization.model.enums.AgeTarget;
import com.dodlu.core.persistence.immunization.entity.*;
import com.dodlu.core.persistence.immunization.repository.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImmunizationService {
  private final ParentRepository parentRepository;
  private final ChildRepository childRepository;
  private final VaccineRepository vaccineRepository;
  private final ImmunizationRepository immunizationRepository;
  private final ImmunizationRecordRepository immunizationRecordRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<ParentModel> createParent(CreateParentRequest request) {
    return supplyAsync(
        () -> {
          doCreateParentValidation(request);
          Parent parent = buildParentFromRequest(request);
          return parentRepository.save(parent).toModel(null);
        });
  }

  @Transactional
  public CompletableFuture<ParentModel> getSingleParent(SearchAttributes searchAttributes) {
    return supplyAsync(
        () -> {
          if (searchAttributes.getId() != null) {
            Parent parent =
                parentRepository
                    .findById(searchAttributes.getId())
                    .orElseThrow(
                        () ->
                            exceptionService.handleCreateNotFoundException(
                                "Parent details does not exist"));
            Set<Child> children = childRepository.findAllByParentId(parent.getId());
            return parent.toModel(children);
          }
          if (searchAttributes.getEmail() != null) {
            Parent parent =
                parentRepository
                    .findByEmailEqualsIgnoreCase(searchAttributes.getEmail())
                    .orElseThrow(
                        () ->
                            exceptionService.handleCreateNotFoundException(
                                "Parent details does not exist"));
            Set<Child> children = childRepository.findAllByParentId(parent.getId());
            return parent.toModel(children);
          }
          if (searchAttributes.getPhoneNumber() != null) {
            Parent parent =
                parentRepository
                    .findByPhoneNumberEquals(searchAttributes.getPhoneNumber())
                    .orElseThrow(
                        () ->
                            exceptionService.handleCreateNotFoundException(
                                "Parent details does not exist"));
            Set<Child> children = childRepository.findAllByParentId(parent.getId());
            return parent.toModel(children);
          }
          throw new IllegalArgumentException(
              "A query parameter for id, email, or phoneNumber is required.");
        });
  }

  @Transactional
  public CompletableFuture<ChildWithParentModel> getSingleChild(String childId) {
    return supplyAsync(
        () -> {
          Child child =
              childRepository
                  .findById(childId)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException(
                              "Child details does not exist"));

          return child.toChildWithParentModel();
        });
  }

  @Transactional
  public CompletableFuture<ParentModel> createChild(String parentId, CreateChildRequest request) {
    return supplyAsync(
        () -> {
          Parent parent =
              parentRepository
                  .findById(parentId)
                  .orElseThrow(
                      () -> exceptionService.handleCreateBadRequest("Parent does not exist"));
          Set<Child> children = childRepository.findAllByParentId(parent.getId());

          Child child = buildChildFromRequest(request);
          ensureNoChildDuplicate(children, child);
          child.setParent(parent);
          child = childRepository.save(child);

          List<ImmunizationRecord> records = generateImmunizationRecordForNewChild(child);
          records = immunizationRecordRepository.saveAll(records);
          child.setImmunizations(extractImmunizationFromRecord(records));
          children.add(child);
          return parent.toModel(children);
        });
  }

  private Set<Immunization> extractImmunizationFromRecord(List<ImmunizationRecord> records) {
    Set<Immunization> immunizations = new HashSet<>();
    for (ImmunizationRecord record : records) {
      immunizations.add(record.getImmunization());
    }
    return immunizations;
  }

  private List<ImmunizationRecord> generateImmunizationRecordForNewChild(Child child) {
    LocalDate today = LocalDate.now();
    LocalDate sixWeeksFromToday = today.plusWeeks(6);
    LocalDate tenWeeksFromToday = today.plusWeeks(10);
    LocalDate fourteenWeeksFromToday = today.plusWeeks(14);
    LocalDate sixMonthsFromToday = today.plusMonths(6);
    LocalDate nineMonthsFromToday = today.plusMonths(9);
    LocalDate twelveMonthsFromToday = today.plusMonths(12);
    LocalDate fifteenMonthsFromToday = today.plusMonths(15);
    LocalDate nineYearsFromToday = today.plusYears(9);
    LocalDate minimumAdministerDate;
    LocalDate childDateOfBirth = child.getDateOfBirth();
    List<ImmunizationRecord> newChildImmunoRecords = new ArrayList<>();

    // Check if child is born today
    if (childDateOfBirth.isEqual(today)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.BIRTH);
      minimumAdministerDate = today;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 6 weeks
    if (childDateOfBirth.isBefore(sixWeeksFromToday)
        || childDateOfBirth.isEqual(sixWeeksFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.WEEK_SIX);
      minimumAdministerDate = sixWeeksFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 10 weeks
    if (childDateOfBirth.isBefore(tenWeeksFromToday)
        || childDateOfBirth.isEqual(tenWeeksFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.WEEK_TEN);
      minimumAdministerDate = tenWeeksFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 14 weeks
    if (childDateOfBirth.isBefore(fourteenWeeksFromToday)
        || childDateOfBirth.isEqual(fourteenWeeksFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.WEEK_FOURTEEN);
      minimumAdministerDate = fourteenWeeksFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 6 months
    if (childDateOfBirth.isBefore(sixMonthsFromToday)
        || childDateOfBirth.isEqual(sixMonthsFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.MONTH_SIX);
      minimumAdministerDate = sixMonthsFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 9 months
    if (childDateOfBirth.isBefore(nineMonthsFromToday)
        || childDateOfBirth.isEqual(nineMonthsFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.MONTH_NINE);
      minimumAdministerDate = nineMonthsFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 12 months
    if (childDateOfBirth.isBefore(twelveMonthsFromToday)
        || childDateOfBirth.isEqual(twelveMonthsFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.MONTH_TWELVE);
      minimumAdministerDate = twelveMonthsFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 15 months
    if (childDateOfBirth.isBefore(fifteenMonthsFromToday)
        || childDateOfBirth.isEqual(fifteenMonthsFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.MONTH_FIFTEEN);
      minimumAdministerDate = fifteenMonthsFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    // Check if child is less than or equal to 9 years
    if (childDateOfBirth.isBefore(nineYearsFromToday)
        || childDateOfBirth.isEqual(nineYearsFromToday)) {
      Collection<Vaccine> vaccinesForAgeTarget =
          vaccineRepository.findAllByAgeTarget(AgeTarget.YEAR_NINE);
      minimumAdministerDate = nineYearsFromToday;
      for (Vaccine vaccine : vaccinesForAgeTarget) {
        Immunization immunization = buildImmunizationFromVaccine(vaccine, minimumAdministerDate);
        immunization = immunizationRepository.save(immunization);
        ImmunizationRecord record = buildImmunizationRecord(immunization, child);
        newChildImmunoRecords.add(record);
      }
    }

    return newChildImmunoRecords;
  }

  private ImmunizationRecord buildImmunizationRecord(Immunization immunization, Child child) {
    return ImmunizationRecord.builder().immunization(immunization).child(child).build();
  }

  private Immunization buildImmunizationFromVaccine(
      Vaccine vaccine, LocalDate minimumAdministerDate) {
    return Immunization.builder()
        .vaccine(vaccine)
        .minimumAdministerDate(minimumAdministerDate)
        .administered(false)
        .dateOfAdministration(null)
        .build();
  }

  private void ensureNoChildDuplicate(Set<Child> children, Child child) {
    for (Child c : children) {
      if (c.getFirstName().equalsIgnoreCase(child.getFirstName())
          && c.getLastName().equalsIgnoreCase(child.getLastName())
          && c.getDateOfBirth().equals(child.getDateOfBirth())) {
        throw exceptionService.handleCreateBadRequest(
            "This is a duplicate record. Child already exists with parent");
      }
    }
  }

  private Parent buildParentFromRequest(CreateParentRequest request) {
    return Parent.builder()
        .title(request.getTitle())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .phoneNumber(request.getPhoneNumber())
        .email(request.getEmail().toLowerCase())
        .build();
  }

  private Child buildChildFromRequest(CreateChildRequest request) {
    return Child.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .gender(request.getGender())
        .dateOfBirth(request.getDateOfBirth())
        .build();
  }

  private void doCreateParentValidation(CreateParentRequest request) {
    ensureParentEmailIsUnique(request.getEmail());
    ensureParentPhoneIsUnique(request.getPhoneNumber());
  }

  private void ensureParentPhoneIsUnique(String phoneNumber) {
    if (parentRepository.existsByPhoneNumberEqualsIgnoreCase(phoneNumber)) {
      throw exceptionService.handleCreateBadRequest(
          "A parent with this phone number already exists");
    }
  }

  private void ensureParentEmailIsUnique(String email) {
    if (parentRepository.existsByEmailEqualsIgnoreCase(email)) {
      throw exceptionService.handleCreateBadRequest("A parent with this email already exists");
    }
  }
}
