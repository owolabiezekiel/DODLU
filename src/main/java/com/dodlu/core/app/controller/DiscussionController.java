package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.discussion.CreateCommentRequest;
import com.dodlu.core.app.model.request.discussion.CreateDiscussionRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.discussion.model.CommentModel;
import com.dodlu.core.domain.discussion.model.DiscussionModel;
import com.dodlu.core.domain.discussion.service.DiscussionService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/discussions", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class DiscussionController {
  private final DiscussionService discussionService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Create discussion in a group")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = DiscussionModel.class)})
  public ResponseModel<DiscussionModel> createGroupDiscussion(
      @AuthenticationPrincipal User user, @Validated @RequestBody CreateDiscussionRequest request) {
    var discussion = discussionService.createDiscussion(user, request);
    return new ResponseModel<>(discussion);
  }

  @PostMapping(value = "/{discussionId}", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Create comment on a discussion in a group")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = CommentModel.class)})
  public ResponseModel<CommentModel> createGroupDiscussionComment(
      @AuthenticationPrincipal User user,
      @PathVariable long discussionId,
      @Validated @RequestBody CreateCommentRequest request) {
    var discussion = discussionService.createDiscussionComment(user, discussionId, request);
    return new ResponseModel<>(discussion);
  }

  @GetMapping("/{groupId}")
  @Operation(description = "Get group discussion")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = DiscussionModel.class,
        responseContainer = "Page")
  })
  public ResponseModel<Page<DiscussionModel>> getGroupDiscussions(
      @AuthenticationPrincipal User user,
      @PathVariable("groupId") String groupId,
      @PageableDefault Pageable pageable) {
    var discussions = discussionService.getGroupDiscussions(user, groupId, pageable);
    return new ResponseModel<>(discussions);
  }

  @GetMapping("/{groupId}/{discussionId}")
  @Operation(description = "Get group discussion by id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = DiscussionModel.class)})
  public ResponseModel<DiscussionModel> getGroupDiscussion(
      @AuthenticationPrincipal User user,
      @PathVariable("groupId") String groupId,
      @PathVariable("discussionId") long discussionId) {
    var discussions = discussionService.getGroupDiscussionById(user, groupId, discussionId);
    return new ResponseModel<>(discussions);
  }
}
