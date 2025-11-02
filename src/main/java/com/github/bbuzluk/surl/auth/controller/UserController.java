package com.github.bbuzluk.surl.auth.controller;

import com.github.bbuzluk.surl.auth.data.dto.CreateUserRequest;
import com.github.bbuzluk.surl.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public void createUser(@RequestBody CreateUserRequest request) {
    userService.createUser(request);
  }
}
