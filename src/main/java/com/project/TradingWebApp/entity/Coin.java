package com.project.TradingWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
/**
 * Represents a cryptocurrency coin entity, storing detailed information about a specific cryptocurrency.
 * This entity is designed to map data from a cryptocurrency API (like CoinGecko, judging by the JSON property names).
 * It includes various metrics such as price, market capitalization, supply, and historical high/low data.
 */
public class Coin {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @JsonProperty("id")
    /**
     * The unique identifier of the cryptocurrency.
     * This ID is typically provided by the cryptocurrency data API and is used to uniquely identify each coin.
     * It serves as the primary key for this entity in the database.
     */
    private String id;

    @Column(name = "symbol", nullable = false)
    @JsonProperty("symbol")
    /**
     * The shorthand symbol or ticker of the cryptocurrency (e.g., "BTC" for Bitcoin, "ETH" for Ethereum).
     * This is a standardized symbol used in exchanges and markets to refer to the cryptocurrency.
     */
    private String symbol;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    /**
     * The full name of the cryptocurrency (e.g., "Bitcoin", "Ethereum").
     * This is the descriptive name of the cryptocurrency.
     */
    private String name;

    @Column(name = "image_url")
    @JsonProperty("image")
    /**
     * URL of the image or logo representing the cryptocurrency.
     * This can be used to display the coin's logo in the user interface.
     */
    private String image;

    @Column(name = "current_price")
    @JsonProperty("current_price")
    /**
     * The current trading price of the cryptocurrency in a specified currency (usually USD).
     * This is the real-time price of the coin in the market.
     */
    private Double currentPrice;

    @Column(name = "market_cap")
    @JsonProperty("market_cap")
    /**
     * The total market capitalization of the cryptocurrency.
     * Calculated as the current price multiplied by the circulating supply.
     * It represents the total market value of all circulating coins.
     */
    private Long marketCap;

    @Column(name = "market_cap_rank")
    @JsonProperty("market_cap_rank")
    /**
     * The rank of the cryptocurrency based on its market capitalization.
     * Coins are ranked from highest market cap (rank 1) to lowest.
     */
    private Integer marketCapRank;

    @Column(name = "fully_diluted_valuation")
    @JsonProperty("fully_diluted_valuation")
    /**
     * The fully diluted valuation of the cryptocurrency.
     * Calculated as if the entire maximum supply were in circulation at the current price.
     * It's a theoretical market cap if all coins were released.
     */
    private Long fullyDilutedValuation;

    @Column(name = "total_volume")
    @JsonProperty("total_volume")
    /**
     * The total volume of cryptocurrency traded in the last 24 hours.
     * It indicates the total value of coins that have been traded in the specified period, usually in USD.
     */
    private Long totalVolume;

    @Column(name = "high_24h")
    @JsonProperty("high_24h")
    /**
     * The highest price of the cryptocurrency in the last 24 hours.
     */
    private Double high24h;

    @Column(name = "low_24h")
    @JsonProperty("low_24h")
    /**
     * The lowest price of the cryptocurrency in the last 24 hours.
     */
    private Double low24h;

    @Column(name = "price_change_24h")
    @JsonProperty("price_change_24h")
    /**
     * The change in price of the cryptocurrency in the last 24 hours (absolute change).
     */
    private Double priceChange24h;

    @Column(name = "price_change_percentage_24h")
    @JsonProperty("price_change_percentage_24h")
    /**
     * The percentage change in price of the cryptocurrency in the last 24 hours.
     */
    private Double priceChangePercentage24h;

    @Column(name = "market_cap_change_24h")
    @JsonProperty("market_cap_change_24h")
    /**
     * The change in market capitalization of the cryptocurrency in the last 24 hours (absolute change).
     */
    private Long marketCapChange24h;

    @Column(name = "market_cap_change_percentage_24h")
    @JsonProperty("market_cap_change_percentage_24h")
    /**
     * The percentage change in market capitalization of the cryptocurrency in the last 24 hours.
     */
    private Double marketCapChangePercentage24h;

    @Column(name = "circulating_supply")
    @JsonProperty("circulating_supply")
    /**
     * The number of coins that are currently in public circulation and are available to trade.
     */
    private Long circulatingSupply;

    @Column(name = "total_supply")
    @JsonProperty("total_supply")
    /**
     * The total number of coins that have been created (minus any burned coins).
     * This may or may not be all in circulation.
     */
    private Long totalSupply;

    @Column(name = "max_supply")
    @JsonProperty("max_supply")
    /**
     * The maximum total number of coins that will ever exist for this cryptocurrency.
     * If null, it means there is no maximum supply.
     */
    private Long maxSupply;

    @Column(name = "ath")
    @JsonProperty("ath")
    /**
     * All-Time High price of the cryptocurrency.
     * The highest price the coin has ever reached in its history.
     */
    private Double allTimeHigh;

    @Column(name = "ath_change_percentage")
    @JsonProperty("ath_change_percentage")
    /**
     * The percentage change from the current price to the all-time high price.
     */
    private Double allTimeHighChangePercentage;

    @Column(name = "ath_date")
    @JsonProperty("ath_date")
    /**
     * The date and time when the cryptocurrency reached its all-time high price.
     */
    private LocalDateTime allTimeHighDate;

    @Column(name = "atl")
    @JsonProperty("atl")
    /**
     * All-Time Low price of the cryptocurrency.
     * The lowest price the coin has ever reached in its history.
     */
    private Double allTimeLow;

    @Column(name = "atl_change_percentage")
    @JsonProperty("atl_change_percentage")
    /**
     * The percentage change from the current price to the all-time low price.
     */
    private Double allTimeLowChangePercentage;

    @Column(name = "atl_date")
    @JsonProperty("atl_date")
    /**
     * The date and time when the cryptocurrency reached its all-time low price.
     */
    private LocalDateTime allTimeLowDate;

    @JsonIgnore
    /**
     * Return on Investment data, if available from the API.
     * This field might contain ROI information for different timeframes, but its structure depends on the API's response.
     */
    private String roi;

    @Column(name = "last_updated")
    @JsonProperty("last_updated")
    /**
     * The date and time when the cryptocurrency's data was last updated by the API.
     * This timestamp indicates the freshness of the data.
     */
    private LocalDateTime lastUpdated;
}