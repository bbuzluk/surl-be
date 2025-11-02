package com.github.bbuzluk.surl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurlController {
  @GetMapping("/version")
  public Map<Object, Object> getVersion() {
    Map<Object, Object> versionInfo = new HashMap<>();
    versionInfo.put("version", null);
    versionInfo.put("buildDate", null);
    return versionInfo;
  }
}
