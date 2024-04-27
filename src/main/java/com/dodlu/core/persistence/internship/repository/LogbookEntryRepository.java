package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.Logbook;
import com.dodlu.core.persistence.internship.entity.LogbookEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LogbookEntryRepository extends JpaRepository<LogbookEntry, String> {
  @Query("SELECT e FROM LogbookEntry e WHERE e.logbook.id = :logbookId ORDER BY e.dateTime ASC")
  List<LogbookEntry> findByLogbookId(@Param("logbookId") String logbookId);

  // @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM LogbookEntry e WHERE
  // e.logbook = :logbook AND FUNCTION('DATE', e.dateTime) = :dateTime")
  @Query(
      "SELECT e from LogbookEntry e WHERE e.logbook = :logbook AND FUNCTION('DATE', e.dateTime) = :dateTime")
  Optional<LogbookEntry> findByLogbookAndDate(
      @Param("logbook") Logbook logbook, @Param("dateTime") LocalDateTime dateTime);

  boolean existsByLogbookId(String logbookId);

  @Transactional
  @Modifying
  @Query(
      "UPDATE LogbookEntry l SET l.comment = :comment, l.approved = true WHERE l.weekNumber = :weekNumber")
  int updateCommentForWeek(@Param("comment") String comment, @Param("weekNumber") int weekNumber);
}
