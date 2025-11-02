package com.github.bbuzluk.surl.shortener.data.repository;

import com.github.bbuzluk.surl.shortener.data.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {}
