package com.dodlu.core.domain.location.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.location.CreateCountryRequest;
import com.dodlu.core.domain.location.model.CountryModel;
import com.dodlu.core.persistence.location.entity.Country;
import com.dodlu.core.persistence.location.repository.CountryRepository;
import com.dodlu.core.persistence.location.repository.StateRepository;
import java.util.concurrent.CompletableFuture;
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
}
