package com.project.TradingWebApp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling coin-related requests.
 */
@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves a list of coins with pagination.
     *
     * @param page The page number to retrieve.
     * @return A ResponseEntity containing the list of coins and HTTP status OK.
     * @throws Exception If an error occurs during the retrieval process.
     */
    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false, name = "page") int page) throws Exception {
        List<Coin> coins = coinService.getCoins(page);
        return new ResponseEntity<>(coins, HttpStatus.OK);
    }

    /**
     * Retrieves market chart data for a specific coin.
     *
     * @param coinId The ID of the coin.
     * @param days   The number of days for historical data.
     * @return A ResponseEntity containing the market chart data as a JSON node and HTTP status OK.
     * @throws Exception If an error occurs during the retrieval process.
     */
    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(
            @PathVariable String coinId,
            @RequestParam("days") int days)
            throws Exception {

        String response = coinService.getMarketChart(coinId, days);
        JsonNode node = objectMapper.readTree(response);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    /**
     * Searches for coins based on a keyword.
     *
     * @param keyword The search keyword.
     * @return A ResponseEntity containing the search results as a JSON node and HTTP status OK.
     * @throws Exception If an error occurs during the search process.
     */
    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoins(@RequestParam("q") String keyword) throws Exception {
        String coin = coinService.searchCoin(keyword);
        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    /**
     * Retrieves the top 50 coins by market capitalization rank.
     *
     * @return A ResponseEntity containing the top 50 coins data as a JSON node and HTTP status OK.
     * @throws Exception If an error occurs during the retrieval process.
     */
    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() throws Exception {
        String coin = coinService.getTop50CoinsByMarketCapRank();
        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    /**
     * Retrieves trending coins.
     *
     * @return A ResponseEntity containing the trending coins data as a JSON node and HTTP status OK.
     * @throws Exception If an error occurs during the retrieval process.
     */
    @GetMapping("/treading")
    ResponseEntity<JsonNode> getTreadingCoin() throws Exception {
        String coin = coinService.getTreadingCoins();
        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    /**
     * Retrieves details for a specific coin.
     *
     * @param coinId The ID of the coin.
     * @return A ResponseEntity containing the coin details as a JSON node and HTTP status OK.
     * @throws Exception If an error occurs during the retrieval process.
     */
    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String coin = coinService.getCoinDetails(coinId);
        JsonNode node = objectMapper.readTree(coin);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}