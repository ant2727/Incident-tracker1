package com.seunome.incidenttracker.dto;

import com.seunome.incidenttracker.entity.IncidentPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateIncidentRequest(
    @NotBlank @Size(max = 150) String title,
    @NotBlank @Size(max = 2000) String description,
    @NotNull IncidentPriority priority,
    UUID teamId,
    UUID assignedToId) {}
