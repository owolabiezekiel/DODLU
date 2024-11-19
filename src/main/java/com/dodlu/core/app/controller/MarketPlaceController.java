package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.marketplace.model.MarketPostModel;
import com.dodlu.core.domain.marketplace.service.DefaultMarketPlaceService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
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

  @GetMapping(value = "/posts/all")
  @Operation(description = "Get market posts")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = MarketPostModel.class)})
  public ResponseModel<Page<MarketPostModel>> getMarketPosts(@PageableDefault Pageable pageable) {
    var posts = marketPlaceService.getMarketPosts(pageable).join();
    return new ResponseModel<>(posts);
  }

  @GetMapping(value = "/posts/me")
  @Operation(description = "Get market posts for customer")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = MarketPostModel.class)})
  public ResponseModel<Page<MarketPostModel>> getMarketPosts(
      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
    var posts = marketPlaceService.getMarketPosts(user, pageable).join();
    return new ResponseModel<>(posts);
  }
}
