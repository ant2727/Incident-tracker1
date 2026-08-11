package com.seunome.incidenttracker.repository;

import com.seunome.incidenttracker.entity.Incident;
import com.seunome.incidenttracker.entity.IncidentPriority;
import com.seunome.incidenttracker.entity.IncidentStatus;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IncidentRepository
    extends JpaRepository<Incident, UUID>, JpaSpecificationExecutor<Incident> {

  Page<Incident> findByStatus(IncidentStatus status, Pageable pageable);

  Page<Incident> findByPriority(IncidentPriority priority, Pageable pageable);

  Page<Incident> findByCreatedAtBetween(Instant from, Instant to, Pageable pageable);
}
