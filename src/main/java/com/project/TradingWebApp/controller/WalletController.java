//package com.project.TradingWebApp.controller;
//
//import com.project.TradingWebApp.entity.*;
//import com.project.TradingWebApp.response.PaymentResponse;
//import com.project.TradingWebApp.service.OrderService;
//import com.project.TradingWebApp.service.PaymentOrderService;
//import com.project.TradingWebApp.service.UserService;
//import com.project.TradingWebApp.service.WalletService;
//import com.razorpay.Payment;
//import com.stripe.service.climate.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//
//@RestController
//public class WalletController {
//
//    @Autowired
//    private WalletService walletService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private PaymentOrderService paymentOrderService;
//
//    @GetMapping("/api/wallet")
//    public ResponseEntity<Wallet> getUserWallet(
//            @RequestHeader("Authorization") String token) throws Exception {
//        UserEntity user = userService.findUserByToken(token);
//        Wallet wallet = walletService.getUserWalllet(user);
//
//        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
//    }
//
//    @PutMapping("/api/wallet/{walletId}/transfer")
//    public ResponseEntity<Wallet> walletToWalletTransfer(
//            @RequestHeader("Authorization") String token,
//            @PathVariable Long walletId,
//            @RequestBody WalletTransaction walletTransaction) throws Exception {
//        UserEntity senderUser = userService.findUserByToken(token);
//        Wallet receiverWallet = walletService.findWalletById(walletId);
//        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, walletTransaction.getAmount());
//
//        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
//    }
//
//    @PutMapping("/api/wallet/order/{orderId}/pay")
//    public ResponseEntity<Wallet> payOrderPayment(
//            @RequestHeader("Authorization") String token,
//            @PathVariable Long orderId,
//            @RequestBody WalletTransaction walletTransaction) throws Exception {
//        UserEntity user = userService.findUserByToken(token);
//
//        Order order = orderService.getOrderById(orderId);
//
//        Wallet wallet = walletService.payOrderPayment(order, user);
//        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
//    }
//
//    @PutMapping("/api/wallet/deposit")
//    public ResponseEntity<Wallet> addMoneyToWallet(
//            @RequestHeader("Authorization") String token,
//            @RequestParam(name = "order_id") Long orderId,
//            @RequestParam(name = "payment_id") String paymentId) throws Exception {
//        UserEntity user = userService.findUserByToken(token);
//
//        Wallet wallet = walletService.getUserWalllet(user);
//        PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderById(orderId);
//        Boolean status = paymentOrderService.processPaymentOrder(paymentOrder, paymentId);
//
//        if(wallet.getBalance() == null){
//            wallet.setBalance(BigDecimal.valueOf(0));
//        }
//        if(status){
//
//            wallet = walletService.addBalanceToWallet(wallet,paymentOrder.getAmount());
//        }
//        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
//    }
//}
package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.entity.*;
import com.project.TradingWebApp.service.*;
import com.project.TradingWebApp.domain.WalletTransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    // ✅ Get user's wallet details
    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Wallet wallet = walletService.getUserWalllet(user);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    // ✅ Wallet-to-wallet transfer (and save transaction)
    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction walletTransaction) throws Exception {

        UserEntity senderUser = userService.findUserByToken(token);
        Wallet senderWallet = walletService.getUserWalllet(senderUser);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Long amount = walletTransaction.getAmount();

        // ✅ Generate unique transfer ID if it's null
        String transferId = walletTransaction.getTransferId();
        if (transferId == null) {
            transferId = UUID.randomUUID().toString(); // Generate unique ID
        }

        // Perform transfer and update balances
        Wallet updatedSenderWallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, amount);

        // ✅ Save transaction for sender
        WalletTransaction senderTransaction = new WalletTransaction();
        senderTransaction.setWallet(senderWallet);
        senderTransaction.setType(WalletTransactionType.WALLET_TRANSFER);
        senderTransaction.setDate(LocalDate.now());
        senderTransaction.setTransferId(transferId); // ✅ Set same transferId
        senderTransaction.setPurpose("Sent money to another wallet with intention of " + walletTransaction.getPurpose());
        senderTransaction.setAmount(-amount); // ✅ Deducting money (Negative)
        walletTransactionService.saveTransaction(senderTransaction);

        // ✅ Save transaction for receiver
        WalletTransaction receiverTransaction = new WalletTransaction();
        receiverTransaction.setWallet(receiverWallet);
        receiverTransaction.setType(WalletTransactionType.WALLET_TRANSFER);
        receiverTransaction.setDate(LocalDate.now());
        receiverTransaction.setTransferId(transferId); // ✅ Same transferId as sender
        receiverTransaction.setPurpose("Received money from another wallet with intention of " + walletTransaction.getPurpose());
        receiverTransaction.setAmount(amount); // ✅ Adding money (Positive)
        walletTransactionService.saveTransaction(receiverTransaction);

        return new ResponseEntity<>(updatedSenderWallet, HttpStatus.OK);
    }


    // ✅ Deposit money into wallet
    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addMoneyToWallet(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId) throws Exception {

        UserEntity user = userService.findUserByToken(token);
        Wallet wallet = walletService.getUserWalllet(user);
        PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderById(orderId);
        Boolean status = paymentOrderService.processPaymentOrder(paymentOrder, paymentId);

        if (wallet.getBalance() == null) {
            wallet.setBalance(BigDecimal.ZERO);
        }

        if (status) {
            wallet = walletService.addBalanceToWallet(wallet, paymentOrder.getAmount());

            // ✅ Save wallet transaction
            WalletTransaction transaction = new WalletTransaction();
            transaction.setWallet(wallet);
            transaction.setType(WalletTransactionType.ADD_MONEY);
            transaction.setDate(LocalDate.now());
            transaction.setPurpose("Deposit via payment order ID: " + orderId);
            transaction.setAmount(paymentOrder.getAmount());
            walletTransactionService.saveTransaction(transaction);
        }

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}
