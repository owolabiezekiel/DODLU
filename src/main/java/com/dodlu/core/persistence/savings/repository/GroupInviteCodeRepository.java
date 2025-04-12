package com.dodlu.core.persistence.savings.repository;

import com.dodlu.core.persistence.savings.entity.GroupInviteCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupInviteCodeRepository extends JpaRepository<GroupInviteCode, Long> {
  boolean existsByCode(String code);

  boolean existsByGroupIdAndCustomerId(String groupId, String customerId);

  Optional<GroupInviteCode> findFirstByGroupIdAndCustomerId(String groupId, String customerId);
}
