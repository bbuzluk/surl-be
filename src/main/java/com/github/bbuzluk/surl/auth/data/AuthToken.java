package com.github.bbuzluk.surl.auth.data;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record AuthToken(String username, String token, Instant expiration, boolean isValid) {
  public AuthToken(String username, String token, Instant expiration) {
    this(username, token, expiration, true);
  }

  public AuthToken(String username, String token) {
    this(username, token, Instant.now().plus(4, ChronoUnit.HOURS), true);
  }

  public AuthToken invalidate() {
    return new AuthToken(this.username, this.token, this.expiration, false);
  }

  public boolean isValidToken() {
    return isValid && !isExpired();
  }

  private boolean isExpired() {
    return Instant.now().isAfter(expiration);
  }
}
