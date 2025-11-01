package com.github.bbuzluk.surl.shortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.bbuzluk.surl.auth.service.UserContextService;
import com.github.bbuzluk.surl.shortener.data.repository.ShortUrlRepository;
import com.github.bbuzluk.surl.shortener.exception.FailedUniqueShortCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class ShortenerServiceTest {

  ShortenerService shortenerService;
  @Mock ShortCodeGenerator shortCodeGenerator;
  @Mock ShortUrlRepository shortUrlRepository;
  @Mock UserContextService userContextService;

  @BeforeEach
  void setUp() {
    this.shortenerService =
        new ShortenerService(shortCodeGenerator, shortUrlRepository, userContextService, 5);
  }

  @Test
  void createShortUrl() {
    when(shortCodeGenerator.generate()).thenReturn("abc123");
    when(userContextService.getCurrentUsername()).thenReturn("testuser");

    String shortCode = shortenerService.createShortCode("https://google.com");
    assertNotNull(shortCode);
    assertEquals("abc123", shortCode);
  }

  @Test
  void createShortUrl_MaxAttemptsExceeded() {
    when(shortCodeGenerator.generate()).thenReturn("abc123");
    when(userContextService.getCurrentUsername()).thenReturn("testuser");
    when(shortUrlRepository.save(any()))
        .thenThrow(new DataIntegrityViolationException("Duplicate"));

    assertThrows(
        FailedUniqueShortCodeException.class,
        () -> shortenerService.createShortCode("https://testurl.com"));
  }
}
