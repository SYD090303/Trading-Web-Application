package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {

}
