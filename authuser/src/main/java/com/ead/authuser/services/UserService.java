package com.ead.authuser.services;

import com.ead.authuser.dtos.PageResponse;
import com.ead.authuser.dtos.UserRequest;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface UserService {
  PageResponse<UserRequest.UserResponse> findAll(Pageable pageable);

  UserRequest.UserResponse findById(UUID userId) throws Exception;

  void delete(UUID userId);

  UserRequest.UserResponse updateProfile(final UUID userId, final UserRequest.Update userDto);

  void updatePassword(final UUID userId, final UserRequest.PasswordUpdate updatePasswordDTO);
}
