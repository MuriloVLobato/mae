package com.ecommerce.bolsas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @NotBlank
  private String street;

  @NotBlank
  private String city;

  @NotBlank
  private String state;

  @NotBlank
  @Column(name = "postal_code")
  private String postalCode;

  @NotBlank
  private String country;

  private String number;
  private String complement;
}
