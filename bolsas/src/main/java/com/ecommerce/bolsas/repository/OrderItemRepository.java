package com.ecommerce.bolsas.repository;

import com.ecommerce.bolsas.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
  interface TopProductProjection {
    String getSku();
    Long getQty();
  }

  @Query("select oi.product.sku as sku, sum(oi.quantity) as qty from OrderItem oi group by oi.product.sku order by qty desc")
  List<TopProductProjection> findTopProducts(Pageable pageable);
}
