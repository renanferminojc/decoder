package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {

  UserRequest.UserResponse registerUser(@RequestBody UserRequest.Registration userDto);
}
