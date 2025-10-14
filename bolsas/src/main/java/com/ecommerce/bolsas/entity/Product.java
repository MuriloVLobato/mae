package com.ecommerce.bolsas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_products_sku", columnList = "sku", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  @Id
  @GeneratedValue
  private UUID id;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @Lob
  private String description;

  @NotNull
  @Digits(integer = 19, fraction = 2)
  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal price;

  @NotBlank
  @Column(nullable = false, unique = true, length = 64)
  private String sku;

  @NotNull
  @Min(0)
  @Column(name = "stock_quantity", nullable = false)
  private Integer stockQuantity;

  private String brand;
  private String material;
  private String dimensions;

  @ElementCollection
  @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
  @Column(name = "image_url", length = 2048)
  @Builder.Default
  private List<String> imageUrls = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
