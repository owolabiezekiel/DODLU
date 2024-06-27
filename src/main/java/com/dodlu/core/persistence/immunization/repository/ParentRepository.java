package com.dodlu.core.persistence.immunization.repository;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.persistence.immunization.entity.querydsl.QParent.parent;

import com.dodlu.core.app.model.filters.ParentFilter;
import com.dodlu.core.persistence.immunization.entity.Parent;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ParentRepository
    extends JpaRepository<Parent, String>, QuerydslPredicateExecutor<Parent> {
  boolean existsByEmailEqualsIgnoreCase(String email);

  boolean existsByPhoneNumberEqualsIgnoreCase(String phoneNumber);

  Optional<Parent> findByEmailEqualsIgnoreCase(String email);

  Optional<Parent> findByPhoneNumberEquals(String phoneNumber);

  default Page<Parent> findAllWithSearch(Pageable pageable, String searchKey) {
    BooleanExpression predicate = null;
    if (searchKey != null) {
      predicate =
          parent
              .firstName.containsIgnoreCase(searchKey)
              .or(parent.lastName.containsIgnoreCase(searchKey))
              .or(parent.email.containsIgnoreCase(searchKey))
              .or(parent.phoneNumber.containsIgnoreCase(searchKey));
    }

    return predicate == null ? findAll(pageable) : findAll(predicate, pageable);
  }

  default Page<Parent> findAll(Pageable pageable, ParentFilter filter) {
    BooleanExpression predicate = null;

    if (!isNullOrBlank(filter.getPhoneNumber())) {
      predicate = parent.phoneNumber.containsIgnoreCase(filter.getPhoneNumber());
    }

    if (!isNullOrBlank(filter.getFirstName())) {
      predicate =
          predicate == null
              ? parent.firstName.containsIgnoreCase(filter.getFirstName())
              : predicate.and(parent.firstName.containsIgnoreCase(filter.getFirstName()));
    }

    if (!isNullOrBlank(filter.getLastName())) {
      predicate =
          predicate == null
              ? parent.lastName.containsIgnoreCase(filter.getLastName())
              : predicate.and(parent.lastName.containsIgnoreCase(filter.getLastName()));
    }

    if (!isNullOrBlank(filter.getEmail())) {
      predicate =
          predicate == null
              ? parent.email.containsIgnoreCase(filter.getEmail())
              : predicate.and(parent.email.containsIgnoreCase(filter.getEmail()));
    }

    return predicate == null ? findAll(pageable) : findAll(predicate, pageable);
  }
}
