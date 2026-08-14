package com.seunome.incidenttracker.service;

import com.seunome.incidenttracker.dto.LoginRequest;
import com.seunome.incidenttracker.dto.LoginResponse;
import com.seunome.incidenttracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public LoginResponse login(LoginRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtService.generateToken(userDetails);
    String role = userDetails.getAuthorities().iterator().next().getAuthority();

    return LoginResponse.of(token, userDetails.getUsername(), role);
  }
}
