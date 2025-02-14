package com.project.TradingWebApp.Request;

import com.project.TradingWebApp.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
