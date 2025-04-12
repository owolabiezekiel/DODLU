package com.dodlu.core.persistence.banking.repository;

import com.dodlu.core.persistence.banking.entity.BankAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, Long> {
  Boolean existsByOwnerId(String ownerId);
}
