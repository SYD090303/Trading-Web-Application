package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
/**
 * Represents a WalletTransaction entity, recording each financial transaction that affects a user's wallet balance.
 * This entity provides a detailed log of all wallet activities, including deposits, withdrawals, transfers, and transactions related to asset trading.
 * It is crucial for tracking financial movements within the trading application and for audit purposes.
 */
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * The unique identifier for the wallet transaction record.
     * This ID is automatically generated, typically as a UUID or a database-generated sequence, and serves as the primary key.
     * It is used to uniquely identify each transaction in the database.
     */
    private Long id;

    @ManyToOne
    /**
     * The Wallet entity associated with this transaction.
     * Establishes a many-to-one relationship with the Wallet entity, meaning multiple transactions can be associated with a single wallet.
     * This field links the transaction to the specific wallet that was affected by the transaction.
     */
    private Wallet wallet;

    /**
     * The type of wallet transaction, such as ADD_MONEY, WITHDRAWAL, BUY_ASSET, SELL_ASSET, or WALLET_TRANSFER.
     * Uses the `WalletTransactionType` enum to categorize the transaction, making it easier to understand and process different types of financial activities.
     * This helps in distinguishing between different kinds of operations performed on the wallet.
     */
    private WalletTransactionType type;

    /**
     * The date when the wallet transaction occurred.
     * Stored as `LocalDate` to represent the date without time component.
     * Provides a record of when each transaction took place, important for reporting and historical tracking.
     */
    private LocalDate date;

    /**
     * An identifier for tracking transfers, especially for WALLET_TRANSFER type transactions.
     * This could be used to group related transactions, for example, in a wallet-to-wallet transfer, both the debit and credit transactions could share the same `transferId`.
     * Useful for linking related transactions and for reconciliation purposes.
     */
    private String transferId;

    /**
     * A description or reason for the wallet transaction.
     * Provides additional context about the transaction, such as "Money deposited", "Asset purchase", "Withdrawal request".
     * Helps in understanding the purpose of each transaction and can be used for user-facing transaction history displays.
     */
    private String purpose;

    /**
     * The amount of the transaction.
     * Represents the financial value involved in the transaction. It could be positive (for deposits, asset sales) or negative (for withdrawals, asset purchases).
     * Stored as Long, which might represent the amount in cents or a similar smallest denomination to avoid floating point precision issues. It's important to clarify the currency and unit of this amount (e.g., cents of USD).
     */
    private Long amount; // Consider using BigDecimal for currency to avoid precision issues and for clarity on currency units.

}