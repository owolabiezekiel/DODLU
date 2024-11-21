package com.dodlu.core.persistence.marketplace.repository;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.persistence.marketplace.entity.querydsl.QMarketPost.marketPost;

import com.dodlu.core.app.model.filters.MarketPostFilter;
import com.dodlu.core.persistence.marketplace.entity.MarketPost;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MarketPostRepository
    extends JpaRepository<MarketPost, Long>, QuerydslPredicateExecutor<MarketPost> {
  Page<MarketPost> findAllByMarketer(CustomerProfile marketer, Pageable pageable);

  default Page<MarketPost> findAll(Pageable pageable, MarketPostFilter filter) {
    BooleanExpression predicate = null;

    if (!isNullOrBlank(filter.title())) {
      predicate = marketPost.title.containsIgnoreCase(filter.title());
    }
    if (filter.category() != null) {
      predicate =
          predicate != null
              ? predicate.and(marketPost.category.eq(filter.category()))
              : marketPost.category.eq(filter.category());
    }

    return predicate == null ? findAll(pageable) : findAll(predicate, pageable);
  }
}
