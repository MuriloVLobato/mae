package com.ecommerce.bolsas.controller;

import com.ecommerce.bolsas.entity.Customer;
import com.ecommerce.bolsas.entity.Order;
import com.ecommerce.bolsas.entity.OrderStatus;
import com.ecommerce.bolsas.repository.CustomerRepository;
import com.ecommerce.bolsas.repository.OrderItemRepository;
import com.ecommerce.bolsas.repository.OrderRepository;
import com.ecommerce.bolsas.service.CustomerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final CustomerService customerService;
  private final CustomerRepository customerRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  @GetMapping("/customers")
  public List<Customer> allCustomers() {
    return customerService.listAll();
  }

  @PostMapping("/customers/{id}/disable")
  public ResponseEntity<Void> disableCustomer(@PathVariable UUID id) {
    customerService.disable(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/orders")
  public List<Order> allOrders() {
    return orderRepository.findAll();
  }

  public record UpdateOrderStatusRequest(@NotNull OrderStatus status) {}

  @PostMapping("/orders/{id}/status")
  public Order updateOrderStatus(@PathVariable UUID id, @RequestBody UpdateOrderStatusRequest req) {
    Order order = orderRepository.findById(id).orElseThrow();
    order.setStatus(req.status());
    return orderRepository.save(order);
  }

  @PostMapping("/orders/{id}/cancel")
  public Order cancelOrder(@PathVariable UUID id) {
    Order order = orderRepository.findById(id).orElseThrow();
    order.setStatus(OrderStatus.CANCELED);
    return orderRepository.save(order);
  }

  @GetMapping("/dashboard")
  public Map<String, Object> dashboard() {
    long customers = customerRepository.count();
    long orders = orderRepository.count();
    long paid = orderRepository.countByStatus(OrderStatus.PAID);

    var top = orderItemRepository.findTopProducts(PageRequest.of(0, 5));
    return Map.of(
        "total_customers", customers,
        "total_orders", orders,
        "paid_orders", paid,
        "top_products", top
    );
  }
}
