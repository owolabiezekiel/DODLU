package com.dodlu.core.persistence.immunization.repository;

import com.dodlu.core.domain.immunization.model.enums.AgeTarget;
import com.dodlu.core.persistence.immunization.entity.Vaccine;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<Vaccine, String> {
  Collection<Vaccine> findAllByAgeTarget(AgeTarget ageTarget);
}
