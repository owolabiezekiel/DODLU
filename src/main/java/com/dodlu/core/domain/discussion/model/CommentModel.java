package com.dodlu.core.domain.discussion.model;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CommentModel(
    long id,
    String comment,
    long discussionId,
    String authorId,
    String authorName,
    LocalDateTime createdAt) {}
