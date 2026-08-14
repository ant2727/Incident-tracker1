package com.seunome.incidenttracker.dto;

public record LoginResponse(String token, String tokenType, String email, String role) {

  public static LoginResponse of(String token, String email, String role) {
    return new LoginResponse(token, "Bearer", email, role);
  }
}
