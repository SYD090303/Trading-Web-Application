package com.project.TradingWebApp.controller;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Wallet;
import com.project.TradingWebApp.entity.WalletTransaction;
import com.project.TradingWebApp.service.UserService;
import com.project.TradingWebApp.service.WalletService;
import com.project.TradingWebApp.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/wallet-transactions")
public class WalletTransactionController {
    @Autowired
    private UserService userService; // Service to retrieve user details

    @Autowired
    private WalletService walletService; // Service to retrieve wallet details

    @Autowired
    private WalletTransactionService walletTransactionService;

    @GetMapping
    public ResponseEntity<?> getUserWalletTransactions(@RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Wallet wallet = walletService.getUserWalllet(user);
        List<WalletTransaction> walletTransactions = walletTransactionService.getTransactionsByWalletId(wallet.getId());
        if (walletTransactions.isEmpty()) {
            return new ResponseEntity<>("No transactions found for this wallet", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(walletTransactions, HttpStatus.OK);
    }

}



