package com.dodlu.core.domain.discussion.model;

import lombok.Builder;

@Builder
public record CommentModel(long id, String comment, long discussionId) {}
