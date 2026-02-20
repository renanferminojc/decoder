package com.ead.authuser.controllers;

import com.ead.authuser.dtos.PageResponse;
import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;

  @Operation(summary = "List users with pagination")
  @GetMapping
  public ResponseEntity<PageResponse<UserRequest.UserResponse>> getAllUsers(
      @PageableDefault(sort = "created", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
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

  @Operation(summary = "List users with pagination and filter")
  @PostMapping
  public ResponseEntity<PageResponse<UserRequest.UserResponse>> searchUsers(
      @PageableDefault(sort = "created", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestBody UserRequest.UserFilterRequest userFilterRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.searchUsers(userFilterRequest, pageable));
  }
}
