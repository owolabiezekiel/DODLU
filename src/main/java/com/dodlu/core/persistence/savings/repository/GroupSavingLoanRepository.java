package com.dodlu.core.persistence.savings.repository;

import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.savings.entity.GroupSavingLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupSavingLoanRepository extends JpaRepository<GroupSavingLoan, String> {
  Page<GroupSavingLoan> findAllByLendingGroup(GroupSaving lendingGroup, Pageable pageable);
}
