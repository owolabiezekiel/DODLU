package com.dodlu.core.domain.immunization.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.vaccines.CreateVaccineRequest;
import com.dodlu.core.domain.immunization.model.VaccineModel;
import com.dodlu.core.domain.utils.VaccineUtils;
import com.dodlu.core.persistence.immunization.entity.Vaccine;
import com.dodlu.core.persistence.immunization.repository.VaccineRepository;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaccineService {
  private final VaccineRepository vaccineRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<Page<VaccineModel>> getAllVaccines(Pageable pageable) {
    return supplyAsync(() -> vaccineRepository.findAll(pageable).map(Vaccine::toModel));
  }

  @Transactional
  public CompletableFuture<VaccineModel> getVaccineById(String id) {
    return supplyAsync(
        () ->
            VaccineUtils.getVaccineById(
                    vaccineRepository,
                    () -> exceptionService.handleCreateNotFoundException("Vaccine does not exist"),
                    id)
                .toModel());
  }

  @Transactional
  public CompletableFuture<VaccineModel> createVaccine(CreateVaccineRequest request) {
    return supplyAsync(
        () -> {
          Vaccine vaccine = buildVaccineFromRequest(request);
          return vaccineRepository.save(vaccine).toModel();
        });
  }

  @Transactional
  public CompletableFuture<VaccineModel> updateVaccine(
      String vaccineId, CreateVaccineRequest request) {
    return supplyAsync(
        () -> {
          Vaccine vaccine =
              VaccineUtils.getVaccineById(vaccineRepository, exceptionService, vaccineId);

          updateVaccineFromRequest(request, vaccine);
          return vaccineRepository.save(vaccine).toModel();
        });
  }

  private void checkNoDuplicateForAgeGroup(CreateVaccineRequest request) {
    Collection<Vaccine> vaccineForTargetAgeSet =
        vaccineRepository.findAllByAgeTarget(request.getAgeTarget());
    for (Vaccine vaccine : vaccineForTargetAgeSet) {
      if (vaccine.getAgeTarget().equals(request.getAgeTarget())
          && vaccine.getType().equals(request.getType())
          && vaccine.getDosage().equals(request.getDosage())
          && vaccine.getDosageType().equals(request.getDosageType())
          && vaccine.getRouteOfAdministration().equals(request.getAdministerRoute())
          && vaccine.getSite().equalsIgnoreCase(request.getAdministerSite())) {
        throw exceptionService.handleCreateBadRequest(
            "Uh oh, looks like a duplicate vaccine is being added here");
      }
    }
  }

  private Vaccine buildVaccineFromRequest(CreateVaccineRequest request) {
    checkNoDuplicateForAgeGroup(request);
    return Vaccine.builder()
        .ageTarget(request.getAgeTarget())
        .type(request.getType())
        .dosage(request.getDosage())
        .dosageType(request.getDosageType())
        .routeOfAdministration(request.getAdministerRoute())
        .site(request.getAdministerSite())
        .build();
  }

  private void updateVaccineFromRequest(CreateVaccineRequest request, Vaccine vaccine) {
    checkNoDuplicateForAgeGroup(request);
    vaccine.setAgeTarget(request.getAgeTarget());
    vaccine.setType(request.getType());
    vaccine.setDosage(request.getDosage());
    vaccine.setDosageType(request.getDosageType());
    vaccine.setRouteOfAdministration(request.getAdministerRoute());
    vaccine.setSite(request.getAdministerSite());
  }
}
