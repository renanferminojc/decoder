package com.ead.authuser.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ead.authuser.BaseIT;
import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.factories.RegistrationBuilder;
import com.ead.authuser.helpers.FilesUtils;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerIT extends BaseIT {

  @Autowired UserRepository userRepository;

  @Nested
  class WhenDatabaseHasOneUser {
    UserModel savedUser;

    @BeforeEach
    void setup() {
      userRepository.deleteAll();
      savedUser = userRepository.save(RegistrationBuilder.valid().buildUserModel());
    }

    @Test
    @DisplayName("Get all users should return page response")
    void case1() throws Exception {
      final String expected = FilesUtils.loadPayload("responses/user/users-page.json");

      final ResultActions resultActions =
          mockMvc
              .perform(get("/users"))
              .andExpect(status().isOk())
              .andExpect(content().json(expected));

      assertAuditFields(resultActions, "$.content[0]");
    }

    @Test
    @DisplayName("Get one user should return a user")
    void case2() throws Exception {
      final String expected = FilesUtils.loadPayload("responses/user/get-user.json");

      final ResultActions resultActions =
          mockMvc
              .perform(get("/users/{userId}", savedUser.getId()))
              .andExpect(status().isOk())
              .andExpect(content().json(expected));
      assertAuditFields(resultActions, "$");
    }

    @Test
    @DisplayName("Update user should update profile")
    void case3() throws Exception {
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
    @DisplayName("Update password should return no content and persist new password")
    void case4() throws Exception {
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
    @DisplayName("Delete user should return no content and remove from data base")
    void case5() throws Exception {
      mockMvc
          .perform(delete("/users/{userId}", savedUser.getId()))
          .andExpect(status().isNoContent());

      assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
    }
  }

  @Nested
  class WhenDatabaseHasAnyUsers {
    UserModel savedUser1;
    UserModel savedUser2;

    @BeforeEach
    void setup() {
      userRepository.deleteAll();
      savedUser1 = userRepository.save(RegistrationBuilder.valid().buildUserModel());
      savedUser2 =
          userRepository.save(
              RegistrationBuilder.valid()
                  .username("user2")
                  .cpf("12345678910")
                  .email("user2@mail.com")
                  .buildUserModel());
    }

    @Test
    @DisplayName("Search users without filters should return all users")
    void case6() throws Exception {
      UserRequest.UserFilterRequest filter =
          new UserRequest.UserFilterRequest(null, null, null, null);

      ResultActions actions =
          mockMvc
              .perform(
                  post("/users")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(filter)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.content", hasSize(2)))
              .andExpect(jsonPath("$.total_elements").value(2))
              .andExpect(jsonPath("$.total_pages").value(1));

      assertAuditFields(actions, "$.content[0]");
    }

    @Test
    @DisplayName("Search users filtered by email should return only matching user")
    void case7() throws Exception {
      var filter = new UserRequest.UserFilterRequest(savedUser2.getEmail(), null, null, null);

      ResultActions actions =
          mockMvc
              .perform(
                  post("/users")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(filter)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.content", hasSize(1)))
              .andExpect(jsonPath("$.total_elements").value(1))
              .andExpect(jsonPath("$.content[0].email").value(savedUser2.getEmail()))
              .andExpect(jsonPath("$.content[0].username").value(savedUser2.getUsername()));

      assertAuditFields(actions, "$.content[0]");
    }

    @Test
    @DisplayName("Search users with pagination should return one item per page")
    void case8() throws Exception {
      var filter = new UserRequest.UserFilterRequest(null, null, null, null);

      mockMvc
          .perform(
              post("/users")
                  .queryParam("page", "0")
                  .queryParam("size", "1")
                  .queryParam("sort", "created," + Sort.Direction.ASC.name().toLowerCase())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(filter)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content", hasSize(1)))
          .andExpect(jsonPath("$.total_elements").value(2))
          .andExpect(jsonPath("$.total_pages").value(2));
    }
  }
}
