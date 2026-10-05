package com.laclippers.admin.repository;

import com.laclippers.admin.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}