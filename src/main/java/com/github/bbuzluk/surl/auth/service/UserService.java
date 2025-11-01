package com.github.bbuzluk.surl.auth.service;

import com.github.bbuzluk.surl.auth.data.dto.CreateUserRequest;
import com.github.bbuzluk.surl.auth.data.entity.User;
import com.github.bbuzluk.surl.auth.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user != null) return user;
    throw new UsernameNotFoundException("User with username " + username + " not found");
  }

  public void createUser(CreateUserRequest user) {
    String encodedPassword = passwordEncoder.encode(user.password());
    User newUser = User.from(user.username(), encodedPassword, user.email());
    userRepository.save(newUser);
  }
}
