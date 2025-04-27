package com.dodlu.core.persistence.savings.repository;

import com.dodlu.core.domain.savings.enums.LoanStatus;
import com.dodlu.core.domain.savings.enums.RepaymentStatus;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.savings.entity.GroupSavingLoan;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupSavingLoanRepository extends JpaRepository<GroupSavingLoan, String> {
  Page<GroupSavingLoan> findAllByLendingGroup(GroupSaving lendingGroup, Pageable pageable);

  long countByLendingGroup_IdAndRepaymentStatus(
      String lendingGroupId, RepaymentStatus repaymentStatus);

  long countByLendingGroup_IdAndLoanStatus(String lendingGroupId, LoanStatus loanStatus);

  @Query(
      "SELECT COALESCE(SUM(gl.amount), 0) FROM GroupSavingLoan gl WHERE gl.lendingGroup.id = :groupId AND gl.loanStatus = :status")
  BigDecimal sumApprovedLoanAmountByGroupIdAndStatus(
      @Param("groupId") String groupId, @Param("status") LoanStatus status);
}
