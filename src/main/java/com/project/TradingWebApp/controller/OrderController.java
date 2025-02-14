package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.Request.CreateOrderRequest;
import com.project.TradingWebApp.domain.OrderType;
import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.Order;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.service.CoinService;
import com.project.TradingWebApp.service.OrderService;
import com.project.TradingWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest request) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Coin coin = coinService.findById(request.getCoinId());

        Order order = orderService.processOrder(coin, request.getQuantity(), request.getOrderType(), user);

        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId
    ) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Order order = orderService.getOrderById(orderId);
        if(order.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
        }
        else{
            throw new Exception("You don't have permission to access this order");
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    )throws Exception{
        Long userId = userService.findUserByToken(token).getId();

        List<Order> userOrders = orderService.getAllOrdersOfUser(userId, order_type, asset_symbol);
        return new ResponseEntity<>(userOrders, HttpStatus.ACCEPTED);
    }
}
