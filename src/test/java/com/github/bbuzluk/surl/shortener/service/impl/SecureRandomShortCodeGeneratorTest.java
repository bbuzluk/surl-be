package com.github.bbuzluk.surl.shortener.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SecureRandomShortCodeGeneratorTest {

  SecureRandomShortCodeGenerator generator = new SecureRandomShortCodeGenerator();

  @Test
  void generate() {
    String shortCode = generator.generate();
    assertNotNull(shortCode);
    assertEquals(6, shortCode.length());
    assertTrue(shortCode.matches("^[A-Za-z0-9]{6}$"));
  }
}
