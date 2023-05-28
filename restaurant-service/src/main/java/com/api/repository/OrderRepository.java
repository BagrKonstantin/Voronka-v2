package com.api.repository;

import com.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findTopByStatusNotOrderByCreatedAtAsc(String status);
}
