package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.Order;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Wallet;

/**
 * Interface for managing user wallets.
 */
public interface WalletService {

    /**
     * Retrieves the wallet associated with a user.
     *
     * @param user The user whose wallet is to be retrieved.
     * @return The user's Wallet object.
     */
    Wallet getUserWalllet(UserEntity user);

    /**
     * Adds balance to a wallet.
     *
     * @param wallet The wallet to which balance is added.
     * @param amount The amount to add to the balance.
     * @return The updated Wallet object.
     */
    Wallet addBalanceToWallet(Wallet wallet, Long amount);

    /**
     * Finds a wallet by its ID.
     *
     * @param id The ID of the wallet.
     * @return The Wallet object if found.
     * @throws Exception If an error occurs during the retrieval process. Consider more specific exception types (e.g., WalletNotFoundException).
     */
    Wallet findWalletById(Long id) throws Exception;

    /**
     * Transfers funds from one wallet to another.
     *
     * @param sender       The user initiating the transfer.
     * @param receiverWallet The wallet to receive the funds.
     * @param amount       The amount to transfer.
     * @return The sender's updated Wallet object.
     * @throws Exception If an error occurs during the transfer process (e.g., insufficient balance, invalid transfer).  Consider more specific exception types.
     */
    Wallet walletToWalletTransfer(UserEntity sender, Wallet receiverWallet, Long amount) throws Exception;

    /**
     * Processes payment for an order using the user's wallet.
     *
     * @param order The order for which payment is being processed.
     * @param user  The user making the payment.
     * @return The user's updated Wallet object after payment.
     * @throws Exception If an error occurs during the payment process (e.g., insufficient balance). Consider more specific exception types.
     */
    Wallet payOrderPayment(Order order, UserEntity user) throws Exception;
}