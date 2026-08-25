package com.seunome.incidenttracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seunome.incidenttracker.IntegrationTestBase;
import com.seunome.incidenttracker.entity.IncidentPriority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

class IncidentControllerTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "admin@incidenttracker.local", roles = "ADMIN")
  void adminDeveConseguirCriarIncidente() throws Exception {
    String body =
        objectMapper.writeValueAsString(
            new IncidentPayload(
                "Servidor fora do ar", "API retornando 500 desde 10h", IncidentPriority.CRITICAL));

    mockMvc
        .perform(post("/incidents").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "VIEWER")
  void viewerNaoDeveConseguirCriarIncidente() throws Exception {
    String body =
        objectMapper.writeValueAsString(
            new IncidentPayload(
                "Tentativa indevida", "Viewer nao pode reportar", IncidentPriority.LOW));

    mockMvc
        .perform(post("/incidents").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "VIEWER")
  void viewerDeveConseguirListarIncidentes() throws Exception {
    mockMvc.perform(get("/incidents")).andExpect(status().isOk());
  }

  @Test
  void semAutenticacaoNaoDeveAcessarIncidentes() throws Exception {
    mockMvc.perform(get("/incidents")).andExpect(status().isUnauthorized());
  }

  private record IncidentPayload(String title, String description, IncidentPriority priority) {}
}
