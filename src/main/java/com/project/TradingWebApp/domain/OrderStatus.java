package com.project.TradingWebApp.domain;

/**
 * Enumeration representing the status of an order in the trading application.
 * Defines the possible states an order can be in during its lifecycle.
 */
public enum OrderStatus {
    /**
     * Order is initially placed and waiting to be processed or executed.
     * It has not yet been sent to the exchange or matched with a counter order.
     */
    PENDING,

    /**
     * Order has been completely executed and filled.
     * The entire quantity of the order has been traded successfully.
     */
    FILLED,

    /**
     * Order has been cancelled by the user or the system.
     * No further execution will occur for this order.
     */
    CANCELLED,

    /**
     * Order has been partially executed.
     * Only a portion of the requested quantity has been traded so far.
     * The order may still be active for the remaining quantity.
     */
    PARTIALLY_FILLED,

    /**
     * Order encountered an error during processing or execution.
     * This could be due to various reasons such as insufficient funds, market conditions, or system issues.
     * Further investigation may be required to understand the cause of the error.
     */
    ERROR,

    /**
     * Order has been successfully processed and executed without any issues.
     * This is a general success status and might be used when the exact fill status (FILLED, PARTIALLY_FILLED) is not immediately relevant.
     * In many cases, `FILLED` would be more specific and preferred for a fully executed order.
     */
    SUCCESS
}