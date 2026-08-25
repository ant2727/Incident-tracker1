package com.seunome.incidenttracker.dto;

import com.seunome.incidenttracker.entity.Incident;
import com.seunome.incidenttracker.entity.IncidentHistory;
import com.seunome.incidenttracker.entity.IncidentPriority;
import com.seunome.incidenttracker.entity.IncidentStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record IncidentResponse(
    UUID id,
    String title,
    String description,
    IncidentStatus status,
    IncidentPriority priority,
    String reportedByName,
    String assignedToName,
    String teamName,
    String resolutionNotes,
    Instant createdAt,
    Instant updatedAt,
    Instant resolvedAt,
    List<HistoryEntry> history) {

  public record HistoryEntry(
      IncidentStatus previousStatus,
      IncidentStatus newStatus,
      String changedByName,
      String note,
      Instant changedAt) {

    public static HistoryEntry from(IncidentHistory history) {
      return new HistoryEntry(
          history.getPreviousStatus(),
          history.getNewStatus(),
          history.getChangedBy().getName(),
          history.getNote(),
          history.getChangedAt());
    }
  }

  public static IncidentResponse from(Incident incident, List<IncidentHistory> historyEntries) {
    return new IncidentResponse(
        incident.getId(),
        incident.getTitle(),
        incident.getDescription(),
        incident.getStatus(),
        incident.getPriority(),
        incident.getReportedBy().getName(),
        incident.getAssignedTo() != null ? incident.getAssignedTo().getName() : null,
        incident.getTeam() != null ? incident.getTeam().getName() : null,
        incident.getResolutionNotes(),
        incident.getCreatedAt(),
        incident.getUpdatedAt(),
        incident.getResolvedAt(),
        historyEntries.stream().map(HistoryEntry::from).toList());
  }

  public static IncidentResponse summary(Incident incident) {
    return from(incident, List.of());
  }
}
