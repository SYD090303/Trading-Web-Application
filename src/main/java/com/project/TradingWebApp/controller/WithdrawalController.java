package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.domain.WalletTransactionType;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Wallet;
import com.project.TradingWebApp.entity.WalletTransaction;
import com.project.TradingWebApp.entity.Withdrawal;
import com.project.TradingWebApp.repository.WalletTransactionRepository;
import com.project.TradingWebApp.service.UserService;
import com.project.TradingWebApp.service.WalletService;
import com.project.TradingWebApp.service.WalletTransactionService;
import com.project.TradingWebApp.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;


    @Autowired
    private WalletTransactionService walletTransactionService;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrwalRequest(@PathVariable Long amount,
                                              @RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Wallet userWallet = walletService.getUserWalllet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalanceToWallet(userWallet,-withdrawal.getAmount());

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setAmount(withdrawal.getAmount());
        walletTransaction.setWallet(userWallet);
        walletTransaction.setPurpose("Bank account withdrawal");
        walletTransaction.setType(WalletTransactionType.WITHDRAWAL);
        walletTransaction.setTransferId(null);
        walletTransactionRepository.save(walletTransaction);
        return new ResponseEntity<>(withdrawal,HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> processWithdrawal(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @PathVariable boolean accept
    ) throws Exception {
      UserEntity user = userService.findUserByToken(token);
      Withdrawal withdrawal = withdrawalService.processWithdrawal(id, accept);
      Wallet userWallet = walletService.getUserWalllet(user);
      if(!accept){
          walletService.addBalanceToWallet(userWallet, withdrawal.getAmount());
      }
      return new ResponseEntity<>(withdrawal,HttpStatus.OK);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawals,HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAdminWithdrawalRequest(@RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalRequest();
        return new ResponseEntity<>(withdrawals,HttpStatus.OK);
    }

}
