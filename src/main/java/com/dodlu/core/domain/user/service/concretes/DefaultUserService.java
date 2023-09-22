package com.dodlu.core.domain.user.service.concretes;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNullStream;
import static java.lang.String.format;

import com.dodlu.core.domain.user.service.abstracts.UserService;
import com.dodlu.core.persistence.user.entity.AppUser;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService, UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser user =
        userRepository
            .findByUsernameIgnoreCase(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(format("User %s does not exist", username)));

    return new User(user.getUsername(), user.getPassword(), extractRoles(user));
  }

  Collection<? extends GrantedAuthority> extractRoles(AppUser user) {
    return emptyIfNullStream(user.getRoles())
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }
}
