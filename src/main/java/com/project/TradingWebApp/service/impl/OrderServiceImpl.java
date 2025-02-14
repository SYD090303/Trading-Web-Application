package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.domain.OrderStatus;
import com.project.TradingWebApp.domain.OrderType;
import com.project.TradingWebApp.entity.*;
import com.project.TradingWebApp.repository.OrderItemRepository;
import com.project.TradingWebApp.repository.OrderRepository;
import com.project.TradingWebApp.service.AssetService;
import com.project.TradingWebApp.service.OrderService;
import com.project.TradingWebApp.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    public Order createOrder(UserEntity user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderType(orderType);
        order.setOrderItem(orderItem);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findAllOrdersOfUserByUserId(userId);
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, UserEntity user) throws Exception {
        if (orderType.equals(OrderType.BUY)) {
            return buyAsset(coin, quantity, user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin, quantity, user);
        } else {
            throw new RuntimeException("Unsupported order type");
        }
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, UserEntity user) throws Exception {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }
        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);

        walletService.payOrderPayment(order, user);

        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order updatedOrder = orderRepository.save(order);

        Asset oldAsset = assetService.getAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
        if (oldAsset == null) {
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
        } else {
            assetService.updateAsset(oldAsset.getId(), orderItem.getQuantity());
        }

        return updatedOrder;

    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, UserEntity user) throws Exception {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }
        Asset assetToSell = assetService.getAssetByUserIdAndCoinId(user.getId(), coin.getId());
        double buyPrice = assetToSell.getBuyPrice();
        double sellPrice = coin.getCurrentPrice();
        if (assetToSell != null) {


            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);

            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {
                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order updatedOrder = orderRepository.save(order);
                walletService.payOrderPayment(order, user);
                Asset updateedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);

                if (updateedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updateedAsset.getId());
                }
                return updatedOrder;
            } else {
                throw new RuntimeException("Insufficient Quantity to sell");
            }
        }
        throw new RuntimeException("Asset not found");
    }

    private OrderItem createOrderItem(Coin coin,
                                      double quantity, double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }
}

