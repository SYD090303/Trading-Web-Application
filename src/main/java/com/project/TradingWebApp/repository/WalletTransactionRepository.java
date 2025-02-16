package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    // Custom query method to find transactions by wallet ID
    List<WalletTransaction> findByWalletId(Long walletId);

    // Custom query method to find transactions by type
    List<WalletTransaction> findByType(com.project.TradingWebApp.domain.WalletTransactionType type);
}
