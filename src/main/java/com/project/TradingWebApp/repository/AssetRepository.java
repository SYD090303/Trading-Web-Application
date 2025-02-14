package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.Asset;
import com.project.TradingWebApp.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUserId(Long userId);
    Asset findByUserIdAndCoinId(Long userId, String coinId);
}
