package com.dodlu.core.persistence.immunization.repository;

import static com.dodlu.core.persistence.immunization.entity.querydsl.QVaccine.vaccine;

import com.dodlu.core.domain.immunization.model.enums.AgeTarget;
import com.dodlu.core.persistence.immunization.entity.Vaccine;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VaccineRepository
    extends JpaRepository<Vaccine, String>, QuerydslPredicateExecutor<Vaccine> {
  Collection<Vaccine> findAllByAgeTarget(AgeTarget ageTarget);

  default Page<Vaccine> findAllWithSearch(Pageable pageable, String searchKey) {
    BooleanExpression predicate = null;
    if (searchKey != null) {
      String sanitizedSearch = searchKey.replace(" ", "_");
      System.out.println(sanitizedSearch);
      predicate =
          vaccine
              .site
              .containsIgnoreCase(searchKey)
              .or(vaccine.ageTarget.stringValue().containsIgnoreCase(searchKey))
              .or(vaccine.type.stringValue().containsIgnoreCase(searchKey))
              .or(vaccine.routeOfAdministration.stringValue().containsIgnoreCase(sanitizedSearch));
    }

    return predicate == null ? findAll(pageable) : findAll(predicate, pageable);
  }
}
