package com.seunome.incidenttracker.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seunome.incidenttracker.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class AuthControllerTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void deveLogarComCredenciaisDoAdminSeedadoERetornarToken() throws Exception {
    String body =
        objectMapper.writeValueAsString(
            new LoginPayload("admin@incidenttracker.local", "changeme123"));

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token", notNullValue()))
        .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
  }

  @Test
  void deveRetornar401ParaSenhaErrada() throws Exception {
    String body =
        objectMapper.writeValueAsString(
            new LoginPayload("admin@incidenttracker.local", "senha-errada"));

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isUnauthorized());
  }

  private record LoginPayload(String email, String password) {}
}
