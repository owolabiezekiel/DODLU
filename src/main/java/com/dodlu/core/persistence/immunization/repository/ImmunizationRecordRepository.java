package com.dodlu.core.persistence.immunization.repository;

import com.dodlu.core.persistence.immunization.entity.ImmunizationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImmunizationRecordRepository extends JpaRepository<ImmunizationRecord, String> {}
