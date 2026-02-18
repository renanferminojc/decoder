package com.ead.authuser.models;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "users")
public class UserModel implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Column(nullable = false, length = 150)
  private String fullName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus userStatus;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserType userType;

  @Column(length = 20)
  private String phoneNumber;

  @Column(length = 20)
  private String cpf;

  @Column private String imageUrl;

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime created;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updated;
}
