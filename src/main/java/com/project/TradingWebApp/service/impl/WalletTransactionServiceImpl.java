package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.WalletTransaction;
import com.project.TradingWebApp.repository.WalletTransactionRepository;
import com.project.TradingWebApp.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public List<WalletTransaction> getAllTransactions() {
        return walletTransactionRepository.findAll();
    }

    @Override
    public Optional<WalletTransaction> getTransactionById(Long id) {
        return walletTransactionRepository.findById(id);
    }

    @Override
    public List<WalletTransaction> getTransactionsByWalletId(Long walletId) {
        return walletTransactionRepository.findByWalletId(walletId);
    }

    @Override
    public WalletTransaction createTransaction(WalletTransaction transaction) {
        return walletTransactionRepository.save(transaction);
    }

    @Override
    public WalletTransaction updateTransaction(Long id, WalletTransaction updatedTransaction) {
        if (walletTransactionRepository.existsById(id)) {
            updatedTransaction.setId(id);
            return walletTransactionRepository.save(updatedTransaction);
        }
        throw new RuntimeException("Transaction with ID " + id + " not found.");
    }

    @Override
    public void deleteTransaction(Long id) {
        walletTransactionRepository.deleteById(id);
    }

    // ✅ Add this method to save a WalletTransaction
    public WalletTransaction saveTransaction(WalletTransaction transaction) {
        return walletTransactionRepository.save(transaction);
    }
}