package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.entity.Order;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Wallet;
import com.project.TradingWebApp.entity.WalletTransaction;
import com.project.TradingWebApp.service.OrderService;
import com.project.TradingWebApp.service.UserService;
import com.project.TradingWebApp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(
            @RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Wallet wallet = walletService.getUserWalllet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction walletTransaction) throws Exception {
        UserEntity senderUser = userService.findUserByToken(token);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, walletTransaction.getAmount());

    return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId,
            @RequestBody WalletTransaction walletTransaction) throws Exception {
        UserEntity user = userService.findUserByToken(token);

        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order, user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
