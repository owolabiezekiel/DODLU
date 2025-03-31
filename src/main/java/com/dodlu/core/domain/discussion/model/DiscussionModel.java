package com.dodlu.core.domain.discussion.model;

import java.util.Set;
import lombok.Builder;

@Builder
public record DiscussionModel(long id, String title, String body, Set<CommentModel> comments) {}
