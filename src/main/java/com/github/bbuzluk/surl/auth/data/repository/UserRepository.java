package com.github.bbuzluk.surl.auth.data.repository;

import com.github.bbuzluk.surl.auth.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
