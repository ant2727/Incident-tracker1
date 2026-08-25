package com.seunome.incidenttracker.service;

import com.seunome.incidenttracker.dto.CreateUserRequest;
import com.seunome.incidenttracker.dto.UserResponse;
import com.seunome.incidenttracker.entity.Team;
import com.seunome.incidenttracker.entity.User;
import com.seunome.incidenttracker.repository.TeamRepository;
import com.seunome.incidenttracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final TeamRepository teamRepository;
  private final PasswordEncoder passwordEncoder;

  public UserResponse create(CreateUserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Já existe um usuário com esse email");
    }

    Team team = null;
    if (request.teamId() != null) {
      team =
          teamRepository
              .findById(request.teamId())
              .orElseThrow(
                  () -> new EntityNotFoundException("Time não encontrado: " + request.teamId()));
    }

    User user =
        User.builder()
            .name(request.name())
            .email(request.email())
            .passwordHash(passwordEncoder.encode(request.password()))
            .role(request.role())
            .team(team)
            .active(true)
            .build();

    return UserResponse.from(userRepository.save(user));
  }

  public List<UserResponse> listAll() {
    return userRepository.findAll().stream().map(UserResponse::from).toList();
  }

  public UserResponse getByEmail(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    return UserResponse.from(user);
  }
}
