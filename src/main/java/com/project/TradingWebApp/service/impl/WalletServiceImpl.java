package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.domain.OrderType;
import com.project.TradingWebApp.entity.Order;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Wallet;
import com.project.TradingWebApp.repository.WalletRepository;
import com.project.TradingWebApp.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Implementation of the WalletService interface.
 * Provides wallet-related functionalities such as balance management and payments.
 */
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    /**
     * Retrieves the wallet associated with the given user.
     * If the wallet does not exist, a new wallet is created.
     *
     * @param user The user whose wallet needs to be fetched.
     * @return The Wallet object associated with the user.
     */
    @Override
    public Wallet getUserWalllet(UserEntity user) {
        Wallet wallet = walletRepository.findWalletByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);
        }
        return wallet;
    }

    /**
     * Adds balance to a user's wallet.
     *
     * @param wallet The wallet to which the balance is to be added.
     * @param amount The amount to be added.
     * @return The updated Wallet object after adding the balance.
     */
    @Override
    public Wallet addBalanceToWallet(Wallet wallet, Long amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    /**
     * Finds a wallet by its ID.
     *
     * @param id The wallet ID to search for.
     * @return The Wallet object if found.
     * @throws Exception If no wallet is found for the given ID.
     */
    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isEmpty()) {
            throw new Exception("Wallet not found");
        }
        return wallet.get();
    }

    /**
     * Transfers balance from one user's wallet to another.
     *
     * @param sender        The user sending the amount.
     * @param receiverWallet The wallet of the receiving user.
     * @param amount        The amount to be transferred.
     * @return The sender's wallet after deduction.
     * @throws Exception If the sender has insufficient balance.
     */
    @Override
    public Wallet walletToWalletTransfer(UserEntity sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = getUserWalllet(sender);

        // Check if the sender has enough balance
        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Insufficient Balance");
        }

        // Deduct the amount from sender's wallet
        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        // Add the amount to receiver's wallet
        BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);

        return senderWallet;
    }

    /**
     * Processes the payment for an order using the user's wallet balance.
     *
     * @param order The order to be paid.
     * @param user  The user making the payment.
     * @return The updated Wallet object after the transaction.
     * @throws Exception If the user has insufficient balance for the transaction.
     */
    @Transactional
    @Override
    public Wallet payOrderPayment(Order order, UserEntity user) throws Exception {
        Wallet wallet = getUserWallet(user);

        // Log the balance before processing
        System.out.println("🔍 Processing order payment...");
        System.out.println("➡️ Order Type: " + order.getOrderType());
        System.out.println("➡️ Order Price: " + order.getPrice());
        System.out.println("➡️ Wallet Old Balance: " + wallet.getBalance());

        if (order.getOrderType().equals(OrderType.BUY)) {
            // Deduct balance for BUY orders
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("Insufficient funds for this transaction");
            }

            wallet.setBalance(newBalance);
        } else {
            // Add balance for SELL orders
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }

        Wallet updatedWallet = walletRepository.save(wallet);

        // Log the updated balance
        System.out.println("✅ Wallet Updated Successfully! New Balance: " + updatedWallet.getBalance());

        return updatedWallet;
    }

    public Wallet getUserWallet(UserEntity user) {
        return walletRepository.findWalletByUserId(user.getId());
    }


}
