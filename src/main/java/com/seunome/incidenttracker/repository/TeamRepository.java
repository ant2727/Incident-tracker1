package com.seunome.incidenttracker.repository;

import com.seunome.incidenttracker.entity.Team;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, UUID> {}
