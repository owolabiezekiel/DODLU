package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNullStream;
import static java.lang.String.format;

import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public abstract class AuthUserService {
  final UserRepository userRepository;

  public User loadUser(String username) {
    return userRepository
        .findByUsernameEqualsIgnoreCase(username)
        .orElseGet(() -> loadUserByEmail(username));
  }

  public User loadUserByEmail(String email) {
    return userRepository
        .findByEmailEqualsIgnoreCase(email)
        .orElseThrow(() -> new UsernameNotFoundException(format("User %s does not exist", email)));
  }

  public Collection<? extends GrantedAuthority> extractRoles(User user) {
    return emptyIfNullStream(user.getRoles())
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }
}
