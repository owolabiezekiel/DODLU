package com.dodlu.core.persistence.transaction.repository;

import com.dodlu.core.persistence.transaction.entity.GroupBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBalanceRepository extends JpaRepository<GroupBalance, String> {}
