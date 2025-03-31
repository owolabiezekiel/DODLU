package com.dodlu.core.domain.user.service.concretes;

import com.dodlu.core.app.exceptions.ResourceNotFoundException;
import com.dodlu.core.domain.user.model.CustomerProfileModel;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerProfileService {
  private final CustomerProfileRepository customerProfileRepository;

  @Transactional
  public CustomerProfileModel getCustomerByUsername(String username) {
    return customerProfileRepository
        .findByUsernameEqualsIgnoreCase(username)
        .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"))
        .toModel();
  }
}
