package com.ead.authuser.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ead.authuser.BaseIT;
import com.ead.authuser.factories.RegistrationBuilder;
import com.ead.authuser.helpers.FilesUtils;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerIT extends BaseIT {

  @Autowired UserRepository userRepository;

  UserModel savedUser;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
    savedUser = userRepository.save(RegistrationBuilder.valid().buildUserModel());
  }

  @Test
  void getAllUsers_shouldReturnPageResponse() throws Exception {
    final String expected = FilesUtils.loadPayload("responses/user/users-page.json");

    final ResultActions resultActions =
        mockMvc
            .perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected));

    assertAuditFields(resultActions, "$.content[0]");
  }

  @Test
  void getOneUser_shouldReturnUser() throws Exception {
    final String expected = FilesUtils.loadPayload("responses/user/get-user.json");

    final ResultActions resultActions =
        mockMvc
            .perform(get("/users/{userId}", savedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().json(expected));
    assertAuditFields(resultActions, "$");
  }

  @Test
  void updateUser_shouldUpdateProfile() throws Exception {
    final String body = FilesUtils.loadPayload("responses/user/put-user-body.json");
    final String response = FilesUtils.loadPayload("responses/user/put-user-response.json");
    final ResultActions resultActions =
        mockMvc
            .perform(
                put("/users/{userId}", savedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isOk())
            .andExpect(content().json(response));

    assertAuditFields(resultActions, "$");
  }

  @Test
  void updatePassword_shouldReturnNoContent_andPersistNewPassword() throws Exception {
    final String response = FilesUtils.loadPayload("responses/user/put-password-body.json");
    mockMvc
        .perform(
            put("/users/{userId}/password", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

    UserModel reloaded = userRepository.findById(savedUser.getId()).orElseThrow();
    assertEquals("newpass123", reloaded.getPassword());
  }

  @Test
  void deleteUser_shouldReturnNoContent_andRemoveFromDb() throws Exception {
    mockMvc.perform(delete("/users/{userId}", savedUser.getId())).andExpect(status().isNoContent());

    assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
  }
}
