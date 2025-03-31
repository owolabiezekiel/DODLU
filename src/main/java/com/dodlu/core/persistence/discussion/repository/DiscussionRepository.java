package com.dodlu.core.persistence.discussion.repository;

import com.dodlu.core.persistence.discussion.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
  boolean existsByGroup_IdAndTitle(String groupId, String title);
}
