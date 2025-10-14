package com.ecommerce.bolsas.controller;

import com.ecommerce.bolsas.dto.ProductDTO;
import com.ecommerce.bolsas.entity.Product;
import com.ecommerce.bolsas.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public List<Product> list() {
    return productService.listAll();
  }

  @GetMapping("/{id}")
  public Product get(@PathVariable UUID id) {
    return productService.getById(id);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO dto) {
    Product product = Product.builder()
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .sku(dto.sku())
        .stockQuantity(dto.stockQuantity())
        .brand(dto.brand())
        .material(dto.material())
        .dimensions(dto.dimensions())
        .imageUrls(dto.imageUrls())
        .build();
    Product saved = productService.create(product);
    return ResponseEntity.created(URI.create("/api/v1/products/" + saved.getId())).body(saved);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public Product update(@PathVariable UUID id, @Valid @RequestBody ProductDTO dto) {
    Product update = Product.builder()
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .sku(dto.sku())
        .stockQuantity(dto.stockQuantity())
        .brand(dto.brand())
        .material(dto.material())
        .dimensions(dto.dimensions())
        .imageUrls(dto.imageUrls())
        .build();
    return productService.update(id, update);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
