package com.seunome.incidenttracker.service;

import com.seunome.incidenttracker.dto.CreateIncidentRequest;
import com.seunome.incidenttracker.dto.IncidentResponse;
import com.seunome.incidenttracker.dto.UpdateIncidentStatusRequest;
import com.seunome.incidenttracker.entity.Incident;
import com.seunome.incidenttracker.entity.IncidentHistory;
import com.seunome.incidenttracker.entity.IncidentPriority;
import com.seunome.incidenttracker.entity.IncidentStatus;
import com.seunome.incidenttracker.entity.Team;
import com.seunome.incidenttracker.entity.User;
import com.seunome.incidenttracker.repository.IncidentHistoryRepository;
import com.seunome.incidenttracker.repository.IncidentRepository;
import com.seunome.incidenttracker.repository.TeamRepository;
import com.seunome.incidenttracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncidentService {

  private final IncidentRepository incidentRepository;
  private final IncidentHistoryRepository historyRepository;
  private final UserRepository userRepository;
  private final TeamRepository teamRepository;

  @Transactional
  public IncidentResponse create(CreateIncidentRequest request, String reporterEmail) {
    User reporter =
        userRepository
            .findByEmail(reporterEmail)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

    Team team = null;
    if (request.teamId() != null) {
      team =
          teamRepository
              .findById(request.teamId())
              .orElseThrow(
                  () -> new EntityNotFoundException("Time não encontrado: " + request.teamId()));
    }

    User assignee = null;
    if (request.assignedToId() != null) {
      assignee =
          userRepository
              .findById(request.assignedToId())
              .orElseThrow(() -> new EntityNotFoundException("Usuário atribuído não encontrado"));
    }

    Incident incident =
        Incident.builder()
            .title(request.title())
            .description(request.description())
            .priority(request.priority())
            .status(IncidentStatus.OPEN)
            .reportedBy(reporter)
            .team(team)
            .assignedTo(assignee)
            .build();

    incident = incidentRepository.save(incident);

    registerHistory(incident, null, IncidentStatus.OPEN, reporter, "Incidente reportado");

    return IncidentResponse.summary(incident);
  }

  public Page<IncidentResponse> list(
      IncidentStatus status, IncidentPriority priority, Pageable pageable) {
    Page<Incident> page;
    if (status != null) {
      page = incidentRepository.findByStatus(status, pageable);
    } else if (priority != null) {
      page = incidentRepository.findByPriority(priority, pageable);
    } else {
      page = incidentRepository.findAll(pageable);
    }
    return page.map(IncidentResponse::summary);
  }

  public IncidentResponse getById(UUID id) {
    Incident incident = findIncidentOrThrow(id);
    var history = historyRepository.findByIncidentIdOrderByChangedAtAsc(id);
    return IncidentResponse.from(incident, history);
  }

  @Transactional
  public IncidentResponse updateStatus(
      UUID id, UpdateIncidentStatusRequest request, String changedByEmail) {
    Incident incident = findIncidentOrThrow(id);
    User changedBy =
        userRepository
            .findByEmail(changedByEmail)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

    IncidentStatus previousStatus = incident.getStatus();
    incident.setStatus(request.newStatus());

    if (request.resolutionNotes() != null) {
      incident.setResolutionNotes(request.resolutionNotes());
    }

    if (request.newStatus() == IncidentStatus.RESOLVED) {
      incident.setResolvedAt(Instant.now());
    }

    incidentRepository.save(incident);
    registerHistory(incident, previousStatus, request.newStatus(), changedBy, request.note());

    var history = historyRepository.findByIncidentIdOrderByChangedAtAsc(id);
    return IncidentResponse.from(incident, history);
  }

  private void registerHistory(
      Incident incident,
      IncidentStatus previous,
      IncidentStatus newStatus,
      User changedBy,
      String note) {
    IncidentHistory entry =
        IncidentHistory.builder()
            .incident(incident)
            .previousStatus(previous)
            .newStatus(newStatus)
            .changedBy(changedBy)
            .note(note)
            .build();
    historyRepository.save(entry);
  }

  private Incident findIncidentOrThrow(UUID id) {
    return incidentRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Incidente não encontrado: " + id));
  }
}
