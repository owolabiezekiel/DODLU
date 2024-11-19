package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.marketplace.CreateMarketPostRequest;
import com.dodlu.core.app.model.request.marketplace.UpdateMarketPostRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.marketplace.model.MarketPostModel;
import com.dodlu.core.domain.marketplace.service.DefaultMarketPlaceService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/market", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class MarketPlaceController {
  private final DefaultMarketPlaceService marketPlaceService;

  @GetMapping(value = "/items/all")
  @Operation(description = "Get market posts")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = MarketPostModel.class)})
  public ResponseModel<Page<MarketPostModel>> getMarketPosts(@PageableDefault Pageable pageable) {
    var posts = marketPlaceService.getMarketPosts(pageable).join();
    return new ResponseModel<>(posts);
  }

  @GetMapping(value = "/items/me")
  @Operation(description = "Get market posts for customer")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = MarketPostModel.class)})
  public ResponseModel<Page<MarketPostModel>> getMarketPosts(
      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
    var posts = marketPlaceService.getMarketPosts(user, pageable).join();
    return new ResponseModel<>(posts);
  }

  @PostMapping(value = "/items", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Create a Market post")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = MarketPostModel.class)})
  public ResponseModel<MarketPostModel> create(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateMarketPostRequest request) {
    var post = marketPlaceService.create(user, request).join();
    return new ResponseModel<>(post);
  }

  @PutMapping(value = "/items/{itemId}", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Update a market post by Id")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = MarketPostModel.class),
    @ApiResponse(code = 401, message = "Unauthorized")
  })
  public ResponseModel<MarketPostModel> update(
      @AuthenticationPrincipal User user,
      @PathVariable("itemId") long itemId,
      @Valid @RequestBody UpdateMarketPostRequest request) {
    var post = marketPlaceService.update(user, itemId, request).join();
    return new ResponseModel<>(post);
  }

  @GetMapping(value = "/items/{itemId}")
  @Operation(description = "Get a market post by Id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = MarketPostModel.class)})
  public ResponseModel<MarketPostModel> get(@PathVariable("itemId") long itemId) {
    var post = marketPlaceService.get(itemId).join();
    return new ResponseModel<>(post);
  }
}
