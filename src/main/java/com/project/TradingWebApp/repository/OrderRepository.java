package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
   List<Order> findAllOrdersOfUserByUserId(Long userId);
}
