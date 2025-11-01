package com.github.bbuzluk.surl.auth.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.github.bbuzluk.surl.auth.data.AuthToken;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtAuthTokenServiceTest {

  static final String JWT_SECRET = "54d95566390ff1414e6a383a7105f9a190b6d2e632d6ac76";

  JwtAuthTokenService jwtAuthTokenService;

  @BeforeEach
  void setUp() {
    this.jwtAuthTokenService = new JwtAuthTokenService(JWT_SECRET);
  }

  @Test
  void generate() {
    Instant expiration = Instant.now().plusSeconds(180);
    AuthToken result = jwtAuthTokenService.generate("user", expiration);
    assertEquals("user", result.username());
    assertNotNull(result.token());
    assertEquals(expiration, result.expiration());
  }

  @Test
  void isValid() {
    Instant expiration = Instant.now().plusSeconds(180);
    AuthToken authToken = jwtAuthTokenService.generate("user", expiration);
    assertTrue(jwtAuthTokenService.isValid(authToken.token()));
    assertFalse(jwtAuthTokenService.isValid("invalid.token.here"));
  }

  @Test
  void get() {
    Instant expiration = Instant.now().plusSeconds(180);
    AuthToken authToken = jwtAuthTokenService.generate("user", expiration);
    AuthToken fetchedToken = jwtAuthTokenService.get(authToken.token());
    assertNotNull(fetchedToken);
    assertEquals("user", fetchedToken.username());
    assertEquals(authToken.token(), fetchedToken.token());
    assertEquals(expiration, fetchedToken.expiration());

    AuthToken invalidToken = jwtAuthTokenService.get("invalid.token.here");
    assertNull(invalidToken);
  }

  @Test
  void get_shouldReturnAuthTokenWhenValidTokenProvided() {
    Instant expiration = Instant.parse("9999-01-01T00:00:00Z");
    String token = jwtAuthTokenService.createJwtToken("user", Date.from(expiration));
    AuthToken fetchedToken = jwtAuthTokenService.get(token);
    assertNotNull(fetchedToken);
    assertEquals(token, fetchedToken.token());
    assertEquals("user", fetchedToken.username());
    assertEquals(expiration, fetchedToken.expiration());
  }

  @Test
  void get_shouldReturnNullWhenTokenIsNull() {
    AuthToken fetchedToken = jwtAuthTokenService.get(null);
    assertNull(fetchedToken);
  }

  @Test
  void invalidate() {
    Instant expiration = Instant.now().plusSeconds(180);
    AuthToken authToken = jwtAuthTokenService.generate("user", expiration);
    assertTrue(jwtAuthTokenService.isValid(authToken.token()));

    jwtAuthTokenService.invalidate(authToken.token());
    assertFalse(jwtAuthTokenService.isValid(authToken.token()));
  }

  @Test
  void invalidate_shouldHandleNonExistentTokenGracefully() {
    String nonExistentToken = "non.existent.token";
    assertDoesNotThrow(() -> jwtAuthTokenService.invalidate(nonExistentToken));
  }
}
