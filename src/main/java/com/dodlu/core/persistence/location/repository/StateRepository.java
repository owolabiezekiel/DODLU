package com.dodlu.core.persistence.location.repository;

import com.dodlu.core.persistence.location.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, String> {}
