package com.dodlu.core.persistence.transaction.repository;

import com.dodlu.core.persistence.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
  @Query("SELECT t FROM Transaction t WHERE t.senderId = :id OR t.receiverId = :id")
  Page<Transaction> findAllForEntity(@Param("id") String id, Pageable pageable);

  @Query(
      "SELECT t FROM Transaction t WHERE (t.senderId = :userId AND t.receiverId = :groupId) OR (t.senderId = :groupId AND t.receiverId = :userId)")
  Page<Transaction> findUserGroupTransactions(
      @Param("userId") String userId, @Param("groupId") String groupId, Pageable pageable);
}
