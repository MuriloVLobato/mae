package com.ecommerce.bolsas.service;

import com.ecommerce.bolsas.entity.*;
import com.ecommerce.bolsas.repository.OrderRepository;
import com.ecommerce.bolsas.repository.CustomerRepository;
import com.ecommerce.bolsas.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public List<Order> listByCustomerEmail(String email) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    return orderRepository.findByCustomer(customer);
  }

  public Order createOrder(String customerEmail, Map<UUID, Integer> productIdToQuantity, String shippingAddress) {
    Customer customer = customerRepository.findByEmail(customerEmail)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

    Order order = new Order();
    order.setCustomer(customer);
    order.setShippingAddress(shippingAddress);

    BigDecimal total = BigDecimal.ZERO;

    for (Map.Entry<UUID, Integer> entry : productIdToQuantity.entrySet()) {
      UUID productId = entry.getKey();
      int quantity = entry.getValue();

      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));

      if (product.getStockQuantity() < quantity) {
        throw new IllegalArgumentException("Insufficient stock for product: " + product.getSku());
      }

      product.setStockQuantity(product.getStockQuantity() - quantity);

      OrderItem item = new OrderItem();
      item.setOrder(order);
      item.setProduct(product);
      item.setQuantity(quantity);
      item.setUnitPrice(product.getPrice());
      item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

      order.getOrderItems().add(item);
      total = total.add(item.getLineTotal());
    }

    order.setTotalAmount(total);
    order.setStatus(OrderStatus.PAID); // assume payment success for now

    return orderRepository.save(order);
  }

  public Order updateStatus(UUID orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    order.setStatus(status);
    return orderRepository.save(order);
  }
}
