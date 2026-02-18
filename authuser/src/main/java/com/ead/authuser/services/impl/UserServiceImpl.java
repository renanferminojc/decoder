package com.ead.authuser.services.impl;

import com.ead.authuser.dtos.PageResponse;
import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.errors.NotFoundException;
import com.ead.authuser.errors.PasswordMismatchException;
import com.ead.authuser.mappers.users.UserMapper;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private UserMapper userMapper;

  @Override
  public PageResponse<UserRequest.UserResponse> findAll(Pageable pageable) {
    Page<UserModel> page = userRepository.findAll(pageable);

    List<UserRequest.UserResponse> content = userMapper.toResponseList(page.getContent());

    return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages());
  }

  @Override
  public UserRequest.UserResponse findById(final UUID userId) {
    return userRepository
        .findById(userId)
        .map(userMapper::toResponse)
        .orElseThrow(() -> new NotFoundException(("User not found")));
  }

  @Override
  @Transactional
  public void delete(final UUID userId) {
    final var user = findUserOrThrow(userId);

    userRepository.delete(user);
  }

  @Override
  @Transactional
  public UserRequest.UserResponse updateProfile(
      final UUID userId, final UserRequest.Update userDTO) {
    UserModel userModel = findUserOrThrow(userId);

    userMapper.updateUserFromDto(userDTO, userModel);

    final UserModel userUpdated = userRepository.save(userModel);

    return userMapper.toResponse(userUpdated);
  }

  @Override
  @Transactional
  public void updatePassword(
      final UUID userId, final UserRequest.PasswordUpdate updatePasswordDTO) {
    final var user = findUserOrThrow(userId);

    if (!user.getPassword().equalsIgnoreCase(updatePasswordDTO.oldPassword())) {
      throw new PasswordMismatchException("Password don't match");
    }

    user.setPassword(updatePasswordDTO.newPassword());
  }

  private UserModel findUserOrThrow(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }
}
