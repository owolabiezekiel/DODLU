package com.dodlu.core.persistence.immunization.repository;

import com.dodlu.core.persistence.immunization.entity.Child;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, String> {
  Set<Child> findAllByParentId(String parentId);
}
