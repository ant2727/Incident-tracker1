package com.seunome.incidenttracker.dto;

import com.seunome.incidenttracker.entity.IncidentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateIncidentStatusRequest(
    @NotNull IncidentStatus newStatus,
    @Size(max = 500) String note,
    @Size(max = 2000) String resolutionNotes) {}
