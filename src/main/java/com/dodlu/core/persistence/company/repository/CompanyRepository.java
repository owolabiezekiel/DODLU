package com.dodlu.core.persistence.company.repository;

import com.dodlu.core.persistence.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {}
