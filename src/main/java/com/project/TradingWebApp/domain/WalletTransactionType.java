package com.project.TradingWebApp.domain;

/**
 * Enumeration representing the type of transaction that can occur in a user's wallet within the trading application.
 * Defines the different categories of financial operations that can affect a user's wallet balance.
 */
public enum WalletTransactionType {
    /**
     * WITHDRAWAL: Represents a transaction where funds are being removed from the user's wallet and transferred out of the trading platform.
     * This typically involves transferring money from the user's trading account to an external account, such as a bank account or another payment method.
     * A withdrawal decreases the available balance in the user's wallet.
     */
    WITHDRAWAL,

    /**
     * WALLET_TRANSFER: Represents a transaction involving the transfer of funds between different wallets within the trading platform.
     * This could be a transfer between a user's own wallets (if they have multiple) or transfers to other users' wallets on the platform.
     * The net effect on the platform's overall balance is typically zero, but it changes the balances of the involved wallets.
     */
    WALLET_TRANSFER,

    /**
     * ADD_MONEY: Represents a transaction where funds are being added to the user's wallet from an external source.
     * This is typically initiated by the user depositing funds into their trading account using various payment methods (e.g., bank transfer, credit card, etc.).
     * Adding money increases the available balance in the user's wallet.
     */
    ADD_MONEY,

    /**
     * BUY_ASSET: Represents a transaction that occurs when a user purchases a financial asset (e.g., stock, cryptocurrency) using funds from their wallet.
     * This type of transaction decreases the wallet balance by the cost of the asset purchased, including any transaction fees or commissions.
     * It reflects the outflow of funds from the wallet to acquire assets.
     */
    BUY_ASSET,

    /**
     * SELL_ASSET: Represents a transaction that occurs when a user sells a financial asset and the proceeds are credited to their wallet.
     * This type of transaction increases the wallet balance by the revenue from selling the asset, minus any transaction fees or commissions.
     * It reflects the inflow of funds into the wallet from asset sales.
     */
    SELL_ASSET
}