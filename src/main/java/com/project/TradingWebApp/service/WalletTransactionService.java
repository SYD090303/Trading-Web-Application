package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.WalletTransaction;
import java.util.List;
import java.util.Optional;

public interface WalletTransactionService {
    /**
     * Fetch all wallet transactions.
     */
    List<WalletTransaction> getAllTransactions();

    /**
     * Fetch a wallet transaction by its ID.
     */
    Optional<WalletTransaction> getTransactionById(Long id);

    /**
     * Fetch all transactions related to a specific wallet.
     */
    List<WalletTransaction> getTransactionsByWalletId(Long walletId);

    /**
     * Save a new wallet transaction.
     */
    WalletTransaction createTransaction(WalletTransaction transaction);

    /**
     * Update an existing wallet transaction.
     */
    WalletTransaction updateTransaction(Long id, WalletTransaction updatedTransaction);

    /**
     * Delete a wallet transaction by ID.
     */
    void deleteTransaction(Long id);

    public WalletTransaction saveTransaction(WalletTransaction transaction);
}
