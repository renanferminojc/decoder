package com.ead.authuser.repositories.user;

import com.ead.authuser.models.UserModel;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
  public static Specification<UserModel> emailEquals(String email) {
    return (root, query, cb) ->
        (email == null || email.isBlank())
            ? null
            : cb.equal(cb.lower(root.get("email")), email.toLowerCase());
  }

  public static Specification<UserModel> hasRole(String roleName) {
    return (root, query, cb) -> {
      if (roleName == null || roleName.isBlank()) return null;
      var join = root.join("roles");
      query.distinct(true);
      return cb.equal(join.get("name"), roleName);
    };
  }

  public static Specification<UserModel> createdBetween(LocalDateTime from, LocalDateTime to) {
    final String createdField = "created";
    return (root, query, cb) -> {
      if (from == null && to == null) return null;
      if (from != null && to != null) return cb.between(root.get(createdField), from, to);
      if (from != null) return cb.greaterThanOrEqualTo(root.get(createdField), from);
      return cb.lessThanOrEqualTo(root.get(createdField), to);
    };
  }
}
