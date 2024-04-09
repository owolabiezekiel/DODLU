package com.dodlu.core.persistence.location.repository;

import com.dodlu.core.persistence.location.entity.State;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, String> {
  boolean existsByCountryIdAndNameEqualsIgnoreCase(String countryId, String name);

  Set<State> findAllByCountryId(String countryId);
}
