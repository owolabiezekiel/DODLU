package com.dodlu.core.persistence.immunization.repository;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.persistence.immunization.entity.querydsl.QChild.child;

import com.dodlu.core.app.model.filters.ChildFilter;
import com.dodlu.core.persistence.immunization.entity.Child;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChildRepository
    extends JpaRepository<Child, String>, QuerydslPredicateExecutor<Child> {
  Set<Child> findAllByParentId(String parentId);

  default Page<Child> findAll(Pageable pageable, ChildFilter filter) {
    BooleanExpression predicate = null;

    if (filter.getGender() != null) {
      predicate = child.gender.eq(filter.getGender());
    }

    if (!isNullOrBlank(filter.getFirstName())) {
      predicate =
          predicate == null
              ? child.firstName.containsIgnoreCase(filter.getFirstName())
              : predicate.and(child.firstName.containsIgnoreCase(filter.getFirstName()));
    }

    if (!isNullOrBlank(filter.getLastName())) {
      predicate =
          predicate == null
              ? child.lastName.containsIgnoreCase(filter.getLastName())
              : predicate.and(child.lastName.containsIgnoreCase(filter.getLastName()));
    }

    return predicate == null ? findAll(pageable) : findAll(predicate, pageable);
  }

  default Page<Child> findAllWithSearch(Pageable pageable, String searchKey) {
    BooleanExpression predicate = null;
    if (!isNullOrBlank(searchKey)) {
      predicate =
          child
              .firstName
              .containsIgnoreCase(searchKey)
              .or(child.lastName.containsIgnoreCase(searchKey))
              .or(child.gender.stringValue().containsIgnoreCase(searchKey));
    }

    return predicate == null ? findAll(pageable) : findAll(predicate, pageable);
  }
}
