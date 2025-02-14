package com.project.TradingWebApp.service;

import com.project.TradingWebApp.domain.OrderType;
import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.Order;
import com.project.TradingWebApp.entity.OrderItem;
import com.project.TradingWebApp.entity.UserEntity;

import java.util.List;

public interface OrderService {
    Order createOrder(UserEntity user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, UserEntity user) throws Exception;
}
