package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.Coin;
import java.util.List;

/**
 * Interface for Coin-related services.
 * Provides methods to fetch coin data, market charts, search functionality, and trending coins.
 */
public interface CoinService {

    /**
     * Retrieves a paginated list of coins.
     *
     * @param page The page number for paginated results.
     * @return A list of Coin objects.
     * @throws Exception If an error occurs during retrieval.
     */
    List<Coin> getCoins(int page) throws Exception;

    /**
     * Fetches the market chart data for a given coin.
     *
     * @param coinId The ID of the coin.
     * @param days   The number of days for which market data is required.
     * @return A JSON string containing the market chart data.
     * @throws Exception If an error occurs during retrieval.
     */
    String getMarketChart(String coinId, int days) throws Exception;

    /**
     * Retrieves detailed information about a specific coin.
     *
     * @param coinId The ID of the coin.
     * @return A JSON string containing the coin details.
     * @throws Exception If an error occurs during retrieval.
     */
    String getCoinDetails(String coinId) throws Exception;

    /**
     * Finds a coin by its unique ID.
     *
     * @param coinId The ID of the coin.
     * @return The Coin entity if found.
     * @throws Exception If the coin is not found.
     */
    Coin findById(String coinId) throws Exception;

    /**
     * Searches for coins based on a keyword.
     *
     * @param keyword The search term used to find coins.
     * @return A JSON string containing matching coin results.
     * @throws Exception If an error occurs during retrieval.
     */
    String searchCoin(String keyword) throws Exception;

    /**
     * Retrieves the top 50 coins based on market capitalization ranking.
     *
     * @return A JSON string containing the top 50 coins.
     * @throws Exception If an error occurs during retrieval.
     */
    String getTop50CoinsByMarketCapRank() throws Exception;

    /**
     * Fetches the currently trending coins.
     *
     * @return A JSON string containing trending coins.
     * @throws Exception If an error occurs during retrieval.
     */
    String getTreadingCoins() throws Exception;
}
