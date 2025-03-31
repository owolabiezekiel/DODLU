package com.dodlu.core.domain.discussion.model;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

@Builder
public record DiscussionModel(
    long id,
    String title,
    String body,
    Set<CommentModel> comments,
    String authorName,
    String authorId,
    LocalDateTime createdAt) {}
