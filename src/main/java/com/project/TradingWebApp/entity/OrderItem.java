package com.project.TradingWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
/**
 * Represents an OrderItem entity, detailing the specific items included in a trading order.
 * Each OrderItem is associated with a particular cryptocurrency (Coin) and specifies the quantity, buy/sell prices,
 * and is linked back to its parent Order.
 */
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * The unique identifier for the order item.
     * This is a Long type and is automatically generated by the database as the primary key.
     * `GenerationType.IDENTITY` indicates that the database is responsible for generating unique, sequential IDs.
     */
    private Long id;

    /**
     * The quantity of the cryptocurrency in this order item.
     * Represents how much of the specified Coin is being bought or sold in this particular item of the order.
     * Stored as a double to allow for fractional quantities, common in cryptocurrency trading.
     */
    private double quantity;

    @ManyToOne
    /**
     * The Coin entity associated with this order item.
     * Establishes a many-to-one relationship with the Coin entity, meaning multiple order items can refer to the same Coin.
     * This indicates which cryptocurrency is being traded in this specific order item.
     */
    private Coin coin;

    /**
     * The buy price specified for this order item, if it's a buy order.
     * This represents the price at which the cryptocurrency was intended to be bought.
     * It may or may not be the actual executed price depending on the order type and market conditions.
     * Stored as a double to represent currency values.
     */
    private double buyPrice;

    /**
     * The sell price specified for this order item, if it's a sell order.
     * This represents the price at which the cryptocurrency was intended to be sold.
     * It may or may not be the actual executed price depending on the order type and market conditions.
     * Stored as a double to represent currency values.
     */
    private double sellPrice;

    @JsonIgnore
    @OneToOne
    /**
     * The Order entity to which this order item belongs.
     * Establishes a one-to-one relationship with the Order entity.
     * `JsonIgnore` annotation is used to prevent infinite recursion during serialization, as Order also has a reference to OrderItem.
     * This field links the details of this item back to the overall trading order it's a part of.
     */
    private Order order;
}