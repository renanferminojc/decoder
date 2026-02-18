package com.ead.authuser.services.impl;

import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.errors.AlreadyInUseException;
import com.ead.authuser.mappers.users.UserMapper;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired private UserRepository userRepository;

  @Autowired private UserMapper userMapper;

  @Override
  @Transactional
  public UserRequest.UserResponse registerUser(final UserRequest.Registration userDto) {
    if (userRepository.existsByUsername(userDto.username())) {
      throw new AlreadyInUseException("username already in use");
    }

    if (userRepository.existsByEmail(userDto.email())) {
      throw new AlreadyInUseException("email already in use");
    }

    final UserModel user = userMapper.toEntity(userDto);
    user.setUserStatus(UserStatus.ACTIVE);
    user.setUserType(UserType.STUDENT);

    return userMapper.toResponse(userRepository.save(user));
  }
}
