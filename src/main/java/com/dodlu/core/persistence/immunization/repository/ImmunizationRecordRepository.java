package com.dodlu.core.persistence.immunization.repository;

import static com.dodlu.core.persistence.immunization.entity.QImmunizationRecord.immunizationRecord;

import com.dodlu.core.persistence.immunization.entity.ImmunizationRecord;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ImmunizationRecordRepository
    extends JpaRepository<ImmunizationRecord, String>,
        QuerydslPredicateExecutor<ImmunizationRecord> {

  default Page<ImmunizationRecord> fetchImminentImmunizationRecord(Pageable pageable) {
    LocalDate today = LocalDate.now();
    BooleanExpression predicate =
        immunizationRecord
            .immunization
            .minimumAdministerDate
            .eq(today)
            .and(immunizationRecord.immunization.administered.eq(false));

    return findAll(predicate, pageable);
  }

  default Page<ImmunizationRecord> fetchImminentImmunizationRecord(
      Pageable pageable, LocalDate startDate, LocalDate endDate) {
    if (startDate == null) {
      startDate = LocalDate.now();
    }
    if (endDate == null) {
      endDate = LocalDate.now();
    }
    if (startDate.isAfter(endDate)) {
      endDate = startDate;
    }
    BooleanExpression predicate =
        immunizationRecord
            .immunization
            .minimumAdministerDate
            .goe(startDate)
            .and(immunizationRecord.immunization.minimumAdministerDate.loe(endDate))
            .and(immunizationRecord.immunization.administered.eq(false));

    return findAll(predicate, pageable);
  }
}
