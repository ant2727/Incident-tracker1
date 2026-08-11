package com.seunome.incidenttracker.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

/**
 * Registro de auditoria de cada mudança de status de um incidente. Usado para reconstruir a linha
 * do tempo em investigações.
 */
@Entity
@Table(name = "incident_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentHistory {

  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incident_id", nullable = false)
  private Incident incident;

  @Enumerated(EnumType.STRING)
  @Column(name = "previous_status", length = 20)
  private IncidentStatus previousStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "new_status", nullable = false, length = 20)
  private IncidentStatus newStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "changed_by_id", nullable = false)
  private User changedBy;

  @Column(length = 500)
  private String note;

  @Column(name = "changed_at", nullable = false, updatable = false)
  private Instant changedAt;

  @PrePersist
  void onCreate() {
    this.changedAt = Instant.now();
  }
}
