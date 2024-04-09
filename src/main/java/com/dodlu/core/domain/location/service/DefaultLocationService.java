package com.dodlu.core.domain.location.service;

import static com.dodlu.core.domain.utils.LocationUtils.getCountryById;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.location.CreateCountryRequest;
import com.dodlu.core.app.model.request.location.CreateStateRequest;
import com.dodlu.core.domain.location.model.CountryModel;
import com.dodlu.core.domain.location.model.StateModel;
import com.dodlu.core.persistence.location.entity.Country;
import com.dodlu.core.persistence.location.entity.State;
import com.dodlu.core.persistence.location.repository.CountryRepository;
import com.dodlu.core.persistence.location.repository.StateRepository;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultLocationService {
  private final StateRepository stateRepository;
  private final CountryRepository countryRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<CountryModel> createCountry(CreateCountryRequest request) {
    return supplyAsync(
        () -> {
          ensureCountryIsUnique(request);
          Country country =
              Country.builder()
                  .name(request.getName())
                  .iso3Code(request.getIso3Code().toUpperCase())
                  .build();
          return countryRepository.save(country).toModel();
        });
  }

  @Transactional
  public CompletableFuture<StateModel> createState(CreateStateRequest request) {
    return supplyAsync(
        () -> {
          Country country = getCountry(request.getCountryId());
          ensureStateIsUnique(request);
          State state = State.builder().name(request.getName()).country(country).build();
          return stateRepository.save(state).toModel();
        });
  }

  @Transactional
  public CompletableFuture<Set<CountryModel>> getAllCountries() {
    return supplyAsync(
        () ->
            countryRepository.findAll().stream().map(Country::toModel).collect(Collectors.toSet()));
  }

  @Transactional
  public CompletableFuture<Set<StateModel>> getAllStatesForCountry(String countryId) {
    return supplyAsync(
        () ->
            stateRepository.findAllByCountryId(countryId).stream()
                .map(State::toModel)
                .collect(Collectors.toSet()));
  }

  @Transactional
  public CompletableFuture<Set<StateModel>> getAllStates() {
    return supplyAsync(
        () -> stateRepository.findAll().stream().map(State::toModel).collect(Collectors.toSet()));
  }

  private Country getCountry(String countryId) {
    return getCountryById(countryRepository, exceptionService, countryId);
  }

  private void ensureCountryIsUnique(CreateCountryRequest request) {
    String countryName = request.getName();
    String iso3Code = request.getIso3Code();
    log.info(
        "Checking if country name -> {} <- with iso3 code -> {} <- is unique",
        countryName,
        iso3Code);
    if (countryRepository.existsByNameEqualsIgnoreCase(countryName)) {
      throw exceptionService.handleCreateBadRequest("Country with name already exists");
    }
    if (countryRepository.existsByIso3CodeEqualsIgnoreCase(iso3Code)) {
      throw exceptionService.handleCreateBadRequest("Country with iso3 code already exists");
    }
  }

  private void ensureStateIsUnique(CreateStateRequest request) {
    String stateName = request.getName();
    String countryId = request.getCountryId();
    log.info(
        "Checking if state with name -> {} <- and country id -> {} <- is unique",
        stateName,
        countryId);
    if (stateRepository.existsByCountryIdAndNameEqualsIgnoreCase(countryId, stateName)) {
      throw exceptionService.handleCreateBadRequest("State with name already exists for country");
    }
  }
}
