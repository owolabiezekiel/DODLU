package com.dodlu.core.persistence.kyc.repository;

import com.dodlu.core.persistence.kyc.entity.BvnDetails;
import com.dodlu.core.persistence.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BVNDetailsRepository extends JpaRepository<BvnDetails, Long> {
  boolean existsByBvn(String bvn);

  Optional<BvnDetails> findByUser(User user);
}
