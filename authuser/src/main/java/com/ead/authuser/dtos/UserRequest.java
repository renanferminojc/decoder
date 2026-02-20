package com.ead.authuser.dtos;

import com.ead.authuser.utils.Constants;
import com.ead.authuser.validators.annotations.NoWhitespace;
import com.ead.authuser.validators.annotations.RequiredField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRequest {
  record UserResponse(
      UUID id,
      String username,
      String email,
      String cpf,
      @JsonProperty("phone_number") String phoneNumber,
      @JsonProperty("full_name") @Size(min = 6) String fullName,
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.datePattern) String created,
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.datePattern)
          String updated) {}

  record Registration(
      @NoWhitespace @RequiredField(message = "{username.required}") String username,
      @RequiredField(message = "{email.required}") @Email String email,
      @RequiredField(message = "{password.required}") @Size(min = 6, message = "{password.min6}")
          String password,
      @NotBlank String cpf,
      @JsonProperty("phone_number") String phoneNumber,
      @RequiredField(message = "{full_name.required}") @JsonProperty("full_name") @Size(min = 6)
          String fullName) {}

  record Update(
      @RequiredField(message = "{full_name.required}") @JsonProperty("full_name") String fullName,
      @JsonProperty("phone_number") String phoneNumber,
      String cpf) {}

  record PasswordUpdate(
      @RequiredField(message = "{password.required}") @JsonProperty("old_password")
          String oldPassword,
      @RequiredField(message = "{password.required}")
          @JsonProperty("new_password")
          @Size(min = 6, message = "{password.min6}")
          String newPassword) {}

  record UserFilterRequest(
      String email,
      String role,
      @JsonProperty("created_from") @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
          LocalDateTime createdFrom,
      @JsonProperty("created_to") @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
          LocalDateTime createdTo) {}
}
