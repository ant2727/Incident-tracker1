package com.seunome.incidenttracker.dto;

import com.seunome.incidenttracker.entity.RoleName;
import com.seunome.incidenttracker.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    RoleName role,
    String teamName,
    boolean active,
    Instant createdAt) {

  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole(),
        user.getTeam() != null ? user.getTeam().getName() : null,
        user.isActive(),
        user.getCreatedAt());
  }
}
