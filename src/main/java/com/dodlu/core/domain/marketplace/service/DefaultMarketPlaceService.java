package com.dodlu.core.domain.marketplace.service;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByUsername;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.filters.MarketPostFilter;
import com.dodlu.core.app.model.request.file.ImageDownloadRequest;
import com.dodlu.core.app.model.request.marketplace.CreateMarketPostRequest;
import com.dodlu.core.app.model.request.marketplace.UpdateMarketPostRequest;
import com.dodlu.core.domain.file.service.abstracts.StorageService;
import com.dodlu.core.domain.marketplace.model.MarketPostModel;
import com.dodlu.core.persistence.marketplace.entity.MarketPost;
import com.dodlu.core.persistence.marketplace.repository.MarketPostRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultMarketPlaceService {
  private final MarketPostRepository marketPostRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private final CoreExceptionService exceptionService;
  private final StorageService storageService;

  @Transactional
  public CompletableFuture<Page<MarketPostModel>> getMarketPosts(
      Pageable pageable, MarketPostFilter filter) {
    return supplyAsync(() -> getPosts(null, pageable, filter));
  }

  @Transactional
  public CompletableFuture<Page<MarketPostModel>> getMarketPosts(User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByUsername(
                  customerProfileRepository, exceptionService, user.getUsername());
          return getPosts(customerProfile, pageable, null);
        });
  }

  @Transactional
  public CompletableFuture<MarketPostModel> create(User user, CreateMarketPostRequest request) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByUsername(
                  customerProfileRepository, exceptionService, user.getUsername());

          MarketPost marketPost =
              MarketPost.builder()
                  .title(request.getTitle())
                  .photoUrl(request.getPhotoUrl())
                  .price(request.getPrice())
                  .description(request.getDescription())
                  .availableUnit(request.getAvailableUnit())
                  .category(request.getCategory())
                  .marketer(customerProfile)
                  .build();

          return marketPostRepository.save(marketPost).toModel();
        });
  }

  @Transactional
  public CompletableFuture<MarketPostModel> update(
      User user, long id, UpdateMarketPostRequest request) {
    return supplyAsync(
        () -> {
          MarketPost marketPost =
              marketPostRepository
                  .findById(id)
                  .orElseThrow(
                      () -> exceptionService.handleCreateNotFoundException("post does not exist"));

          if (!user.getUsername().equalsIgnoreCase(marketPost.getMarketer().getUsername())) {
            throw exceptionService.handleCreateUnAuthorized("Unauthorized!");
          }

          if (request.getCategory() != null) marketPost.setCategory(request.getCategory());
          if (request.getPrice() != null) marketPost.setPrice(request.getPrice());
          if (!isNullOrBlank(request.getTitle())) marketPost.setTitle(request.getTitle());
          if (!isNullOrBlank(request.getPhotoUrl())) marketPost.setPhotoUrl(request.getPhotoUrl());
          if (!isNullOrBlank(request.getDescription()))
            marketPost.setDescription(request.getDescription());
          if (!isNullOrBlank(request.getAvailableUnit()))
            marketPost.setAvailableUnit(request.getAvailableUnit());

          return marketPostRepository.save(marketPost).toModel();
        });
  }

  @Transactional
  public CompletableFuture<MarketPostModel> get(long id) {
    return supplyAsync(() -> getPost(id).toModel());
  }

  @Transactional
  public CompletableFuture<Void> delete(long id) {
    return runAsync(
        () -> {
          MarketPost item = getPost(id);
          ImageDownloadRequest req = new ImageDownloadRequest().setImageUrl(item.getPhotoUrl());
          storageService.deleteFile(req);
          marketPostRepository.deleteById(id);
        });
  }

  private Page<MarketPostModel> getPosts(
      CustomerProfile marketer, Pageable pageable, MarketPostFilter filter) {
    Page<MarketPost> marketPostPage;

    marketPostPage =
        Objects.isNull(marketer)
            ? marketPostRepository.findAll(pageable, filter)
            : marketPostRepository.findAllByMarketer(marketer, pageable);

    List<MarketPostModel> marketPosts =
        marketPostPage.getContent().stream()
            .map(MarketPost::toModel)
            .distinct()
            .collect(Collectors.toList());
    return new PageImpl<>(marketPosts, pageable, marketPostPage.getTotalElements());
  }

  private MarketPost getPost(long id) {
    return marketPostRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateNotFoundException("post does not exist"));
  }
}
