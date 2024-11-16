package com.samuel.email.signature.generator.repository;

import com.samuel.email.signature.generator.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);
}
