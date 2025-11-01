package com.github.bbuzluk.surl.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;

import com.github.bbuzluk.surl.auth.data.AuthToken;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

class AuthServiceTest {
  AuthService authService;
  @Mock AuthTokenService authTokenService;
  @Mock AuthenticationManager authenticationManager;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.authService =
        new AuthService(Duration.ofSeconds(60), authTokenService, authenticationManager);
  }

  @Test
  void login_shouldReturnAuthTokenWhenCorrectCredentials() {
    when(authTokenService.generate(any(), any())).thenReturn(new AuthToken("user", ""));
    when(authenticationManager.authenticate(any()))
        .thenReturn(authenticated("user", "password", List.of()));

    var authToken = authService.login("user", "password");
    assertEquals("user", authToken.username());
    assertNotNull(authToken.token());
    assertNotNull(authToken.expiration());
  }

  @Test
  void login_shouldThrowAuthenticationExceptionWhenWrongCredentials() {
    when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
    assertThrows(AuthenticationException.class, () -> authService.login("user", "wrongpassword"));
  }
}
