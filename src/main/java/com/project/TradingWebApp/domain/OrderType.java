package com.project.TradingWebApp.domain;

/**
 * Enumeration representing the type of an order in the trading application.
 * Defines whether the order is to buy or sell a financial instrument.
 */
public enum OrderType {
    /**
     * BUY: Represents an order to purchase a financial instrument.
     * When an order is of type BUY, it indicates the user's intention to acquire the specified quantity of the asset.
     * This is also known as a "long" position.
     */
    BUY,

    /**
     * SELL: Represents an order to dispose of or sell a financial instrument.
     * When an order is of type SELL, it indicates the user's intention to liquidate or reduce their holdings of the asset.
     * This is also known as a "short" position when selling borrowed assets, or simply closing a long position.
     */
    SELL
}