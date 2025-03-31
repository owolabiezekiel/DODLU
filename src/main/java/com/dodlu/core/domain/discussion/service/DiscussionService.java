package com.dodlu.core.domain.discussion.service;

import com.dodlu.core.app.exceptions.BadRequestException;
import com.dodlu.core.app.model.request.discussion.CreateDiscussionRequest;
import com.dodlu.core.domain.discussion.model.DiscussionModel;
import com.dodlu.core.domain.savings.service.GroupSavingsService;
import com.dodlu.core.domain.user.service.concretes.CustomerProfileService;
import com.dodlu.core.persistence.discussion.entity.Discussion;
import com.dodlu.core.persistence.discussion.repository.DiscussionRepository;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import java.util.HashSet;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscussionService {
  private final GroupSavingsService groupSavingsService;
  private final CustomerProfileService customerProfileService;
  private final DiscussionRepository discussionRepository;

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
}
