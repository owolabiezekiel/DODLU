package com.dodlu.core.domain.marketplace.service;

import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
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

  @Transactional
  public CompletableFuture<Page<MarketPostModel>> getMarketPosts(Pageable pageable) {
    return supplyAsync(() -> getPosts(null, pageable));
  }

  @Transactional
  public CompletableFuture<Page<MarketPostModel>> getMarketPosts(User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByUsername(
                  customerProfileRepository, exceptionService, user.getUsername());
          return getPosts(customerProfile, pageable);
        });
  }

  private Page<MarketPostModel> getPosts(CustomerProfile marketer, Pageable pageable) {
    Page<MarketPost> marketPostPage;

    marketPostPage =
        Objects.isNull(marketer)
            ? marketPostRepository.findAll(pageable)
            : marketPostRepository.findAllByMarketer(marketer, pageable);

    List<MarketPostModel> marketPosts =
        marketPostPage.getContent().stream()
            .map(MarketPost::toModel)
            .distinct()
            .collect(Collectors.toList());
    return new PageImpl<>(marketPosts, pageable, marketPostPage.getTotalElements());
  }
}
