package com.ead.authuser.factories;

import com.ead.authuser.dtos.UserRequest;

public final class RegistrationBuilder {
  private String username = "joaovitor";
  private String email = "joao@email.com";
  private String password = "123456";
  private String cpf = "12345678900";
  private String phoneNumber = "43111111111";
  private String fullName = "João Vitor";

  private RegistrationBuilder() {}

  public static RegistrationBuilder valid() {
    return new RegistrationBuilder();
  }

  public RegistrationBuilder username(String v) {
    this.username = v;
    return this;
  }

  public RegistrationBuilder email(String v) {
    this.email = v;
    return this;
  }

  public RegistrationBuilder password(String v) {
    this.password = v;
    return this;
  }

  public RegistrationBuilder cpf(String v) {
    this.cpf = v;
    return this;
  }

  public RegistrationBuilder phoneNumber(String v) {
    this.phoneNumber = v;
    return this;
  }

  public RegistrationBuilder fullName(String v) {
    this.fullName = v;
    return this;
  }

  public UserRequest.Registration build() {
    return new UserRequest.Registration(username, email, password, cpf, phoneNumber, fullName);
  }
}
