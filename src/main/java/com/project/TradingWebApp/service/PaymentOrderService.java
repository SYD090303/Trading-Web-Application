package com.project.TradingWebApp.service;

import com.project.TradingWebApp.domain.PaymentMethod;
import com.project.TradingWebApp.entity.PaymentOrder;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentOrderService {
    PaymentOrder createPaymentOrder(UserEntity user, PaymentMethod paymentMethod, Long amount);

    PaymentOrder getPaymentOrderById(Long paymentId);

    Boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(UserEntity user, Long amount) throws RazorpayException;

    PaymentResponse createStripePaymentLink(UserEntity user, Long amount, Long orderId) throws StripeException;
}
