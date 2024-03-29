package com.test.store.authentication.config;

import com.test.store.authentication.entity.User;
import com.test.store.authentication.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  private static final String USER_NOT_FOUND_MSG = "User '%s' not found";

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User dbUser = this.userRepository.findByUsername(username);

    if (Objects.nonNull(dbUser)) {
      Collection<GrantedAuthority> grantedAuthorities =
          dbUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role))
              .collect(Collectors.toList());

      org.springframework.security.core.userdetails.User user =
          new org.springframework.security.core.userdetails.User(
          dbUser.getUsername(), dbUser.getPassword(), grantedAuthorities);
      return user;
    } else {
      throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
    }
  }

}