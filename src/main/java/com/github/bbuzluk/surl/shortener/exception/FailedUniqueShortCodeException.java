package com.github.bbuzluk.surl.shortener.exception;

public class FailedUniqueShortCodeException extends RuntimeException {
  public FailedUniqueShortCodeException(String message) {
    super(message);
  }

  public FailedUniqueShortCodeException() {
    super("Failed to generate unique short code after multiple attempts");
  }
}
