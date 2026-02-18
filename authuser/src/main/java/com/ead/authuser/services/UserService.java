package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {
  List<UserRequest.UserResponse> findAll();

  UserRequest.UserResponse findById(UUID userId) throws Exception;

  void delete(UUID userId);

  UserRequest.UserResponse updateProfile(final UUID userId, final UserRequest.Update userDto);

  void updatePassword(final UUID userId, final UserRequest.PasswordUpdate updatePasswordDTO);
}
