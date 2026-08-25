package com.seunome.incidenttracker.controller;

import com.seunome.incidenttracker.dto.CreateIncidentRequest;
import com.seunome.incidenttracker.dto.IncidentResponse;
import com.seunome.incidenttracker.dto.UpdateIncidentStatusRequest;
import com.seunome.incidenttracker.entity.IncidentPriority;
import com.seunome.incidenttracker.entity.IncidentStatus;
import com.seunome.incidenttracker.service.IncidentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
@Tag(name = "Incidentes")
public class IncidentController {

  private final IncidentService incidentService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
  public ResponseEntity<IncidentResponse> create(
      @Valid @RequestBody CreateIncidentRequest request, Authentication authentication) {
    IncidentResponse created = incidentService.create(request, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping
  public ResponseEntity<Page<IncidentResponse>> list(
      @RequestParam(required = false) IncidentStatus status,
      @RequestParam(required = false) IncidentPriority priority,
      Pageable pageable) {
    return ResponseEntity.ok(incidentService.list(status, priority, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<IncidentResponse> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(incidentService.getById(id));
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
  public ResponseEntity<IncidentResponse> updateStatus(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateIncidentStatusRequest request,
      Authentication authentication) {
    return ResponseEntity.ok(incidentService.updateStatus(id, request, authentication.getName()));
  }
}
