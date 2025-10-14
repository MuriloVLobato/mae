package com.ecommerce.bolsas.repository;

import com.ecommerce.bolsas.entity.Customer;
import com.ecommerce.bolsas.entity.Order;
import com.ecommerce.bolsas.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
  List<Order> findByCustomer(Customer customer);
  long countByStatus(OrderStatus status);
}
