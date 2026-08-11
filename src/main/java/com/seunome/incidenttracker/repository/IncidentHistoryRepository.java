package com.seunome.incidenttracker.repository;

import com.seunome.incidenttracker.entity.IncidentHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentHistoryRepository extends JpaRepository<IncidentHistory, UUID> {

  List<IncidentHistory> findByIncidentIdOrderByChangedAtAsc(UUID incidentId);
}
