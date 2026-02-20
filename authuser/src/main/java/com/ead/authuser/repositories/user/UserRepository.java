package com.ead.authuser.repositories.user;

import com.ead.authuser.models.UserModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
