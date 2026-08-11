CREATE TABLE teams (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE users (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    team_id BINARY(16),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_users_team FOREIGN KEY (team_id) REFERENCES teams (id)
);

CREATE TABLE incidents (
    id BINARY(16) NOT NULL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    reported_by_id BINARY(16) NOT NULL,
    assigned_to_id BINARY(16),
    team_id BINARY(16),
    resolution_notes VARCHAR(2000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP NULL,
    CONSTRAINT fk_incidents_reported_by FOREIGN KEY (reported_by_id) REFERENCES users (id),
    CONSTRAINT fk_incidents_assigned_to FOREIGN KEY (assigned_to_id) REFERENCES users (id),
    CONSTRAINT fk_incidents_team FOREIGN KEY (team_id) REFERENCES teams (id)
);

CREATE TABLE incident_history (
    id BINARY(16) NOT NULL PRIMARY KEY,
    incident_id BINARY(16) NOT NULL,
    previous_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_by_id BINARY(16) NOT NULL,
    note VARCHAR(500),
    changed_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_history_incident FOREIGN KEY (incident_id) REFERENCES incidents (id),
    CONSTRAINT fk_history_changed_by FOREIGN KEY (changed_by_id) REFERENCES users (id)
);

CREATE INDEX idx_incidents_status ON incidents (status);
CREATE INDEX idx_incidents_priority ON incidents (priority);
CREATE INDEX idx_incidents_team ON incidents (team_id);
CREATE INDEX idx_history_incident ON incident_history (incident_id);
