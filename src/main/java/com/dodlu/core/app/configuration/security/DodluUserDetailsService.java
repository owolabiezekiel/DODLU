package com.dodlu.core.app.configuration.security;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNullStream;

import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class DodluUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsernameEqualsIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), extractRoles(user));
  }

  Collection<? extends GrantedAuthority> extractRoles(User user) {
    return emptyIfNullStream(user.getRoles())
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }
}
