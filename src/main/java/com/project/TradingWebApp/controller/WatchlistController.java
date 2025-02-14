package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Watchlist;
import com.project.TradingWebApp.service.CoinService;
import com.project.TradingWebApp.service.UserService;
import com.project.TradingWebApp.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getWatchlist(
            @RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Watchlist watchlist = watchlistService.findUserWatchlistById(user.getId());

        return new ResponseEntity<>(watchlist,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Watchlist> createWatchlist(
            @RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Watchlist createdWatchlist = watchlistService.createWatchlist(user);
        return new ResponseEntity<>(createdWatchlist,HttpStatus.CREATED);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(@PathVariable Long watchlistId) {
        Watchlist watchlist = watchlistService.findUserWatchlistById(watchlistId);
        return new ResponseEntity<>(watchlist,HttpStatus.OK);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addCoinToWatchlist(
            @RequestHeader("Authorization") String token
    , @PathVariable String coinId) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin = watchlistService.addCoinToWatchlist(coin, user);
        return new ResponseEntity<>(addedCoin,HttpStatus.OK);
    }
}
