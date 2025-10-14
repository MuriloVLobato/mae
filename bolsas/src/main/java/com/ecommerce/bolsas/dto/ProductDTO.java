package com.ecommerce.bolsas.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
    @NotBlank String name,
    String description,
    @NotNull @Digits(integer = 19, fraction = 2) BigDecimal price,
    @NotBlank String sku,
    @NotNull @Min(0) Integer stockQuantity,
    String brand,
    String material,
    String dimensions,
    List<@Size(max = 2048) String> imageUrls
) {}
