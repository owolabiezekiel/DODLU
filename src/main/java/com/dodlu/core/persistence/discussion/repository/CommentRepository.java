package com.dodlu.core.persistence.discussion.repository;

import com.dodlu.core.persistence.discussion.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
