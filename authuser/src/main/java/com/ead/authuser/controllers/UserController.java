package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.services.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;

  @GetMapping
  public ResponseEntity<List<UserRequest.UserResponse>> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserRequest.UserResponse> getOneUser(@PathVariable UUID userId)
      throws Exception {
    UserRequest.UserResponse user = userService.findById(userId);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping("/{userId}")
  public ResponseEntity<UserRequest.UserResponse> updateUser(
      @PathVariable UUID userId, @RequestBody UserRequest.Update userDto) {
    final UserRequest.UserResponse user = userService.updateProfile(userId, userDto);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @PutMapping("/{userId}/password")
  public ResponseEntity<Void> updatePassword(
      @PathVariable UUID userId, @RequestBody UserRequest.PasswordUpdate updatePasswordDTO) {
    userService.updatePassword(userId, updatePasswordDTO);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
