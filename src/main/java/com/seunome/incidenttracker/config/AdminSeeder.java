package com.seunome.incidenttracker.config;

import com.seunome.incidenttracker.entity.RoleName;
import com.seunome.incidenttracker.entity.User;
import com.seunome.incidenttracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.admin.email}")
  private String adminEmail;

  @Value("${app.admin.password}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    if (userRepository.existsByEmail(adminEmail)) {
      return;
    }

    User admin =
        User.builder()
            .name("Admin")
            .email(adminEmail)
            .passwordHash(passwordEncoder.encode(adminPassword))
            .role(RoleName.ADMIN)
            .active(true)
            .build();

    userRepository.save(admin);
    log.info("Usuário admin inicial criado: {}", adminEmail);
  }
}
