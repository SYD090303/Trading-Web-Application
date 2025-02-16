package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findWalletByUserId(Long userId);
}
