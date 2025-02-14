package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findWalletByUserId(Long userId);
}
