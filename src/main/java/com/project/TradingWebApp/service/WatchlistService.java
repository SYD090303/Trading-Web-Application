package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Watchlist;

public interface WatchlistService {

    Watchlist getUserWatchlist(Long userId);

    Watchlist createWatchlist(UserEntity user);

    Watchlist findUserWatchlistById(Long id);

    Coin addCoinToWatchlist(Coin coin, UserEntity user);
}
