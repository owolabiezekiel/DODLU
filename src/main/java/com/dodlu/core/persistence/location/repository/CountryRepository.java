package com.dodlu.core.persistence.location.repository;

import com.dodlu.core.persistence.location.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {}
