package com.ecommerce.bolsas.controller;

import com.ecommerce.bolsas.entity.Order;
import com.ecommerce.bolsas.service.OrderService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public List<Order> myOrders(@AuthenticationPrincipal UserDetails principal) {
    return orderService.listByCustomerEmail(principal.getUsername());
  }

  public static record CreateOrderRequest(@NotEmpty Map<UUID, Integer> items, @NotBlank String shippingAddress) {}

  @PostMapping
  public Order create(@AuthenticationPrincipal UserDetails principal, @RequestBody CreateOrderRequest req) {
    return orderService.createOrder(principal.getUsername(), req.items(), req.shippingAddress());
  }
}
