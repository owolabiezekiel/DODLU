package com.dodlu.core.app.configuration.security;

import com.dodlu.core.domain.user.service.concretes.AuthUserService;
import com.dodlu.core.persistence.user.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Primary
@Component
public class DodluUserDetailsPasswordService extends AuthUserService implements UserDetailsService {
  public DodluUserDetailsPasswordService(UserRepository userRepository) {
    super(userRepository);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = loadUser(username);
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), extractRoles(user));
  }
}
