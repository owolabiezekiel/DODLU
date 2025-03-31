package com.dodlu.core.persistence.discussion.repository;

import com.dodlu.core.persistence.discussion.entity.Discussion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
  boolean existsByGroup_IdAndTitle(String groupId, String title);

  @Query("SELECT t from Discussion t WHERE t.group.id = :groupId")
  Page<Discussion> findGroupDiscussions(@Param("groupId") String groupId, Pageable pageable);

  Optional<Discussion> getDiscussionByIdAndGroup_Id(long id, String groupId);
}
