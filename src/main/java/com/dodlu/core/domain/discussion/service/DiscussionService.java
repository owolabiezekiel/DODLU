package com.dodlu.core.domain.discussion.service;

import com.dodlu.core.app.exceptions.BadRequestException;
import com.dodlu.core.app.exceptions.ResourceNotFoundException;
import com.dodlu.core.app.model.request.discussion.CreateCommentRequest;
import com.dodlu.core.app.model.request.discussion.CreateDiscussionRequest;
import com.dodlu.core.domain.discussion.model.CommentModel;
import com.dodlu.core.domain.discussion.model.DiscussionModel;
import com.dodlu.core.domain.savings.service.GroupSavingsService;
import com.dodlu.core.domain.user.service.concretes.CustomerProfileService;
import com.dodlu.core.persistence.discussion.entity.Comment;
import com.dodlu.core.persistence.discussion.entity.Discussion;
import com.dodlu.core.persistence.discussion.repository.CommentRepository;
import com.dodlu.core.persistence.discussion.repository.DiscussionRepository;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import java.util.HashSet;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscussionService {
  private final GroupSavingsService groupSavingsService;
  private final CustomerProfileService customerProfileService;
  private final DiscussionRepository discussionRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public DiscussionModel createDiscussion(User user, CreateDiscussionRequest request) {
    var customer = customerProfileService.getCustomerByUsername(user.getUsername());
    var group = groupSavingsService.getGroupSaving(user, request.getGroupId());
    if (Objects.isNull(group)) {
      throw new BadRequestException("Group does not exist");
    }

    if (discussionRepository.existsByGroup_IdAndTitle(group.id(), request.getTitle())) {
      throw new BadRequestException(
          "Discussion with similar title already exists. please consider checking out that thread");
    }

    Discussion discussion =
        Discussion.builder()
            .title(request.getTitle())
            .body(request.getBody())
            .group(GroupSaving.builder().id(group.id()).build())
            .author(CustomerProfile.builder().id(customer.getId()).build())
            .comments(new HashSet<>())
            .build();
    discussion = discussionRepository.save(discussion);
    return discussion.toModel();
  }

  @Transactional
  public Page<DiscussionModel> getGroupDiscussions(User user, String groupId, Pageable page) {
    var group = groupSavingsService.getGroupSaving(user, groupId);
    if (Objects.isNull(group)) {
      throw new BadRequestException("Group does not exist");
    }

    return discussionRepository.findGroupDiscussions(groupId, page).map(Discussion::toModel);
  }

  @Transactional
  public DiscussionModel getGroupDiscussionById(User user, String groupId, long discussionId) {
    var group = groupSavingsService.getGroupSaving(user, groupId);
    if (Objects.isNull(group)) {
      throw new BadRequestException("Group does not exist");
    }

    return discussionRepository
        .getDiscussionByIdAndGroup_Id(discussionId, groupId)
        .map(Discussion::toModel)
        .orElseThrow(() -> new ResourceNotFoundException("Discussion not found"));
  }

  @Transactional
  public CommentModel createDiscussionComment(
      User user, long discussionId, CreateCommentRequest request) {
    var customer = customerProfileService.getCustomerByUsername(user.getUsername());
    var group = groupSavingsService.getGroupSaving(user, request.getGroupId());
    if (Objects.isNull(group)) {
      throw new BadRequestException("Group does not exist");
    }

    var discussion =
        discussionRepository
            .getDiscussionByIdAndGroup_Id(discussionId, request.getGroupId())
            .orElseThrow(() -> new ResourceNotFoundException("Discussion does not exist"));

    var comment =
        Comment.builder()
            .comment(request.getComment())
            .discussion(discussion)
            .author(CustomerProfile.builder().id(customer.getId()).build())
            .build();
    comment = commentRepository.save(comment);
    return comment.toModel();
  }
}
