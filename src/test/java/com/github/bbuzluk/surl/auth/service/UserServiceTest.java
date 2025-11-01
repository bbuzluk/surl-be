package com.github.bbuzluk.surl.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.github.bbuzluk.surl.auth.data.entity.User;
import com.github.bbuzluk.surl.auth.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserServiceTest {
  @InjectMocks UserService userService;
  @Mock UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void loadUserByUsername() {
    when(userRepository.findByUsername("testuser"))
        .thenReturn(User.from("testuser", "password", "user@mail.com"));
    UserDetails user = userService.loadUserByUsername("testuser");
    assertNotNull(user);
    assertEquals("testuser", user.getUsername());
    assertEquals("password", user.getPassword());
    assertNotNull(user.getAuthorities());
    assertTrue(user.isEnabled());
    assertTrue(user.isAccountNonLocked());
  }

  @Test
  void loadUserByUsername_UserNotFound() {
    when(userRepository.findByUsername("nonexistent")).thenReturn(null);
    assertThrows(
        UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
  }
}
