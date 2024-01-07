package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.LogbookEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogbookEntryRepository extends JpaRepository<LogbookEntry, String> {
  Page<LogbookEntry> findByLogbookId(Pageable pageable, String logbookId);
}
