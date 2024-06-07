package com.dodlu.core.persistence.immunization.repository;

import com.dodlu.core.persistence.immunization.entity.Immunization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImmunizationRepository extends JpaRepository<Immunization, String> {}
