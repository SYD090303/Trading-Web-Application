package com.project.TradingWebApp.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentUrl;
    private Long orderId;
    private String paymentId;
}
