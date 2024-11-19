package com.dodlu.core.persistence.marketplace.repository;

import com.dodlu.core.persistence.marketplace.entity.MarketPost;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPostRepository extends JpaRepository<MarketPost, Long> {
  Page<MarketPost> findAllByMarketer(CustomerProfile marketer, Pageable pageable);
}
