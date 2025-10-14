package com.ecommerce.bolsas.service;

import com.ecommerce.bolsas.entity.Product;
import com.ecommerce.bolsas.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public List<Product> listAll() {
    return productRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Product getById(UUID id) {
    return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }

  public Product create(@Valid Product product) {
    if (productRepository.existsBySku(product.getSku())) {
      throw new IllegalArgumentException("SKU already exists");
    }
    return productRepository.save(product);
  }

  public Product update(UUID id, @Valid Product update) {
    Product existing = getById(id);
    if (!existing.getSku().equals(update.getSku()) && productRepository.existsBySku(update.getSku())) {
      throw new IllegalArgumentException("SKU already exists");
    }
    existing.setName(update.getName());
    existing.setDescription(update.getDescription());
    existing.setPrice(update.getPrice());
    existing.setSku(update.getSku());
    existing.setStockQuantity(update.getStockQuantity());
    existing.setBrand(update.getBrand());
    existing.setMaterial(update.getMaterial());
    existing.setDimensions(update.getDimensions());
    existing.setImageUrls(update.getImageUrls());
    return productRepository.save(existing);
  }

  public void delete(UUID id) {
    Product existing = getById(id);
    productRepository.delete(existing);
  }
}
