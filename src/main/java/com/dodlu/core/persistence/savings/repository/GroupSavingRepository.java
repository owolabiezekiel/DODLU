package com.dodlu.core.persistence.savings.repository;

import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupSavingRepository extends JpaRepository<GroupSaving, String> {
  boolean existsByAdminAndName(CustomerProfile admin, String name);
}
