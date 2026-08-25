package com.seunome.incidenttracker.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

  private static final String SECRET =
      "test-secret-key-needs-to-be-long-enough-for-hs256-algorithm";

  private JwtService jwtService;
  private UserDetails userDetails;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService(SECRET, 60);
    userDetails =
        User.builder()
            .username("analista@incidenttracker.local")
            .password("hash")
            .authorities(new SimpleGrantedAuthority("ROLE_ANALISTA"))
            .build();
  }

  @Test
  void deveGerarTokenEExtrairEmailCorretamente() {
    String token = jwtService.generateToken(userDetails);

    assertThat(token).isNotBlank();
    assertThat(jwtService.extractEmail(token)).isEqualTo("analista@incidenttracker.local");
  }

  @Test
  void deveValidarTokenGeradoParaOMesmoUsuario() {
    String token = jwtService.generateToken(userDetails);

    assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
  }

  @Test
  void naoDeveValidarTokenParaUsuarioDiferente() {
    String token = jwtService.generateToken(userDetails);
    UserDetails outroUsuario =
        User.builder()
            .username("outro@incidenttracker.local")
            .password("hash")
            .authorities(new SimpleGrantedAuthority("ROLE_VIEWER"))
            .build();

    assertThat(jwtService.isTokenValid(token, outroUsuario)).isFalse();
  }

  @Test
  void deveLancarExcecaoParaTokenExpirado() {
    JwtService jwtServiceComExpiracaoCurta = new JwtService(SECRET, 0);
    String token = jwtServiceComExpiracaoCurta.generateToken(userDetails);

    assertThatThrownBy(() -> jwtService.extractEmail(token))
        .isInstanceOf(ExpiredJwtException.class);
  }
}
