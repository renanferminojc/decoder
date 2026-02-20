package com.ead.authuser.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ead.authuser.BaseIT;
import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.factories.RegistrationBuilder;
import com.ead.authuser.repositories.user.UserRepository;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MvcResult;

class AuthenticationControllerIT extends BaseIT {
  @Value("${app.urls.auth.sign-up}")
  private String signUpPath;

  @Value("${app.urls.users.base}")
  private String usersPath;

  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve registrar um usuário com sucesso e salvar no banco")
  void registerUser_Success() throws Exception {
    var registration = RegistrationBuilder.valid().build();

    UUID id = signupAndGetId(registration);

    var userInDb = userRepository.findById(id);
    assertTrue(userInDb.isPresent());

    var u = userInDb.get();
    assertEquals(registration.username(), u.getUsername());
    assertEquals(registration.email(), u.getEmail());
    assertEquals(registration.cpf(), u.getCpf());
    assertEquals(registration.phoneNumber(), u.getPhoneNumber());
    assertEquals(registration.fullName(), u.getFullName());
    assertEquals(UserStatus.ACTIVE, u.getUserStatus());
  }

  static Stream<Arguments> conflictCases() {
    return Stream.of(
        Arguments.of(RegistrationBuilder.valid().build(), "username already in use"),
        Arguments.of(
            RegistrationBuilder.valid().username("another").build(), "email already in use"));
  }

  @ParameterizedTest
  @MethodSource("conflictCases")
  void registerUser_DeveRetornar409(UserRequest.Registration second, String expectedMsg)
      throws Exception {
    signupAndGetId(RegistrationBuilder.valid().build());
    postRequest(signUpPath, second)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(expectedMsg));
  }

  @Test
  void naoDeveConterSenhaNoResponse() throws Exception {
    UUID id = signupAndGetId(RegistrationBuilder.valid().build());

    mockMvc
        .perform(get(usersPath + "/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  void deveRetornar400QuandoEmailInvalido() throws Exception {
    var invalidPayload = RegistrationBuilder.valid().email("invalid_email").build();
    expectBadRequestWithMessages(
        postRequest(signUpPath, invalidPayload), "must be a well-formed email address");
  }

  @Test
  void deveRetornar400QuandoTiverEspacoNoUsername() throws Exception {
    var invalidPayload = RegistrationBuilder.valid().username("with spaces").build();
    expectBadRequestWithMessages(
        postRequest(signUpPath, invalidPayload), "this field should not have spaces");
  }

  @Test
  void deveRetornar400QuandoOsCamposForemInvalido() throws Exception {
    var invalida =
        RegistrationBuilder.valid()
            .username(null)
            .email(null)
            .fullName(null)
            .password("12345")
            .build();

    expectBadRequestWithMessages(
        postRequest(signUpPath, invalida),
        "full name can not be null or empty",
        "username can not be null or empty",
        "password must have at least 6 characters",
        "email can not be null or empty");
  }

  private UUID signupAndGetId(UserRequest.Registration registration) throws Exception {
    MvcResult result =
        postRequest(signUpPath, registration).andExpect(status().isCreated()).andReturn();

    return readBody(result, UserRequest.UserResponse.class).id();
  }
}
