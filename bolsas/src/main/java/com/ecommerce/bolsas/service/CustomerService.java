package com.ecommerce.bolsas.service;

import com.ecommerce.bolsas.entity.Customer;
import com.ecommerce.bolsas.entity.Role;
import com.ecommerce.bolsas.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<Customer> listAll() {
    return customerRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Customer getById(UUID id) {
    return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
  }

  public Customer register(@Valid Customer customer, boolean asAdmin) {
    if (customerRepository.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Email already registered");
    }
    customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    customer.setRole(asAdmin ? Role.ROLE_ADMIN : Role.ROLE_CUSTOMER);
    return customerRepository.save(customer);
  }

  public void disable(UUID id) {
    Customer existing = getById(id);
    existing.setEnabled(false);
    customerRepository.save(existing);
  }
}
