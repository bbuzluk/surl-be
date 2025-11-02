package com.github.bbuzluk.surl.core.api;

public record PageMeta(int page, int size, long totalElements) {
  public int totalPages() {
    return (int) Math.ceil((double) totalElements / size);
  }
}
