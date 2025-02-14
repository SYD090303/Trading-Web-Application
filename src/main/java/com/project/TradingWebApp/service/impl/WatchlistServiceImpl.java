package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Watchlist;
import com.project.TradingWebApp.repository.WatchlistRepository;
import com.project.TradingWebApp.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public Watchlist getUserWatchlist(Long userId) {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if (watchlist == null) {
            throw new RuntimeException("Watchlist not found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(UserEntity user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findUserWatchlistById(Long id) {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        if (watchlist.isPresent()) {
            return watchlist.get();
        }
        throw new RuntimeException("Watchlist not found");
    }

    @Override
    public Coin addCoinToWatchlist(Coin coin, UserEntity user) {
        Watchlist watchlist = findUserWatchlistById(user.getId());
       if(watchlist.getCoins().contains(coin)) {
           watchlist.getCoins().remove(coin);
       }
       else {
           watchlist.getCoins().add(coin);
       }
       watchlistRepository.save(watchlist);
       return coin;
    }
}
