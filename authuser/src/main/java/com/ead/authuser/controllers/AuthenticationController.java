package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired private AuthenticationService authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<UserRequest.UserResponse> registerUser(
      @RequestBody @Valid UserRequest.Registration userDto) {
    var userCreated = authenticationService.registerUser(userDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
