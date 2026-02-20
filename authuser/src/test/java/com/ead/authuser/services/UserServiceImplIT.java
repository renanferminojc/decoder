package com.ead.authuser.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ead.authuser.dtos.PageResponse;
import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.errors.NotFoundException;
import com.ead.authuser.errors.PasswordMismatchException;
import com.ead.authuser.factories.RegistrationBuilder;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplIT {

  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

  private UserModel userSaved;

  @BeforeEach
  void setup() {
    userSaved = userRepository.save(RegistrationBuilder.valid().buildUserModel());
  }

  @Test
  void findAll_shouldReturnPageResponse() {
    PageResponse<UserRequest.UserResponse> res = userService.findAll(PageRequest.of(0, 10));
    assertEquals(1, res.content().size());
    assertEquals(1, res.totalElements());
    assertEquals(1, res.totalPages());
  }

  @Test
  void findById_shouldReturnUserResponse() throws Exception {
    UserRequest.UserResponse dto = userService.findById(userSaved.getId());
    assertEquals(userSaved.getId(), dto.id());
    assertEquals(userSaved.getUsername(), dto.username());
    assertEquals(userSaved.getEmail(), dto.email());
  }

  @Test
  void findById_shouldThrowNotFound() {
    UUID randomId = UUID.randomUUID();
    assertThrows(NotFoundException.class, () -> userService.findById(randomId));
  }

  @Test
  void updateProfile_shouldUpdateAndReturnResponse() {
    final String nameUpdated = "Renan Atualizado";
    final String phoneNumberUpdated = "61988887777";
    final String cpfUpdated = "99999999999";

    UserRequest.Update update = new UserRequest.Update(nameUpdated, phoneNumberUpdated, cpfUpdated);
    UserRequest.UserResponse dto = userService.updateProfile(userSaved.getId(), update);

    assertEquals(nameUpdated, dto.fullName());
    assertEquals(phoneNumberUpdated, dto.phoneNumber());
    assertEquals(cpfUpdated, dto.cpf());

    UserModel reloaded = userRepository.findById(userSaved.getId()).orElseThrow();
    assertEquals(nameUpdated, reloaded.getFullName());
  }

  @Test
  void updatePassword_shouldPersistNewPassword() {
    final String newPass = "newpass123";
    UserRequest.PasswordUpdate pw =
        new UserRequest.PasswordUpdate(userSaved.getPassword(), newPass);
    userService.updatePassword(userSaved.getId(), pw);

    UserModel reloaded = userRepository.findById(userSaved.getId()).orElseThrow();
    assertEquals(newPass, reloaded.getPassword());
  }

  @Test
  void updatePassword_wrongOldPassword_shouldThrow() {
    UserRequest.PasswordUpdate pw = new UserRequest.PasswordUpdate("WRONG", "newpass123");
    assertThrows(
        PasswordMismatchException.class, () -> userService.updatePassword(userSaved.getId(), pw));
  }

  @Test
  void delete_shouldRemoveUser() {
    UUID id = userSaved.getId();
    userService.delete(id);
    assertTrue(userRepository.findById(id).isEmpty());
  }
}
