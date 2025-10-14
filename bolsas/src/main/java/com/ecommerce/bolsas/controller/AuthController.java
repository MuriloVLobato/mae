package com.ecommerce.bolsas.controller;

import com.ecommerce.bolsas.dto.LoginRequest;
import com.ecommerce.bolsas.dto.RegisterRequest;
import com.ecommerce.bolsas.entity.Customer;
import com.ecommerce.bolsas.security.jwt.JwtTokenProvider;
import com.ecommerce.bolsas.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final CustomerService customerService;
  private final JwtTokenProvider tokenProvider;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
    Customer customer = Customer.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .password(request.password())
        .phoneNumber(request.phoneNumber())
        .build();
    Customer saved = customerService.register(customer, false);
    return ResponseEntity.ok(Map.of("id", saved.getId()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = tokenProvider.generateToken(request.email());
    return ResponseEntity.ok(Map.of("access_token", token, "token_type", "Bearer"));
  }
}
