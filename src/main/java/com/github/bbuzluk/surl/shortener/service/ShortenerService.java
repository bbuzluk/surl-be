package com.github.bbuzluk.surl.shortener.service;

import com.github.bbuzluk.surl.auth.service.UserContextService;
import com.github.bbuzluk.surl.shortener.data.entity.ShortUrl;
import com.github.bbuzluk.surl.shortener.data.repository.ShortUrlRepository;
import com.github.bbuzluk.surl.shortener.exception.FailedUniqueShortCodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ShortenerService {
  private final ShortCodeGenerator shortCodeGenerator;
  private final ShortUrlRepository shortUrlRepository;
  private final UserContextService userContextService;
  private final int maxAttempts;

  public ShortenerService(
      ShortCodeGenerator shortCodeGenerator,
      ShortUrlRepository shortUrlRepository,
      UserContextService userContextService,
      @Value("${shorturl.max-attempts:5}") int maxAttempts) {
    this.shortCodeGenerator = shortCodeGenerator;
    this.shortUrlRepository = shortUrlRepository;
    this.userContextService = userContextService;
    this.maxAttempts = maxAttempts;
  }

  public String createShortCode(String originalUrl) {
    String username = userContextService.getCurrentUsername();
    ShortUrl shortUrl = ShortUrl.create(originalUrl, username, shortCodeGenerator.generate());
    saveWithRetry(shortUrl);
    return shortUrl.getShortCode();
  }

  private void saveWithRetry(ShortUrl shortUrl) {
    for (int i = 0; i < maxAttempts; i++) {
      try {
        shortUrlRepository.save(shortUrl);
        return;
      } catch (DataIntegrityViolationException e) {
        shortUrl.setShortCode(shortCodeGenerator.generate());
      }
    }
    throw new FailedUniqueShortCodeException();
  }
}
