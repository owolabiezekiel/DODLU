package com.dodlu.core.persistence.location.repository;

import com.dodlu.core.persistence.location.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {
  boolean existsByNameEqualsIgnoreCase(String name);

  boolean existsByIso3CodeEqualsIgnoreCase(String iso3Code);
}
