package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.domain.PaymentMethod;
import com.project.TradingWebApp.entity.PaymentOrder;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.response.PaymentResponse;
import com.project.TradingWebApp.service.PaymentOrderService;
import com.project.TradingWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String token
            ) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        PaymentResponse paymentResponse;

        PaymentOrder order = paymentOrderService.createPaymentOrder(user, paymentMethod, amount);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse = paymentOrderService.createRazorpayPaymentLink(user, amount);
        }
        else{
            paymentResponse = paymentOrderService.createStripePaymentLink(user, amount, order.getId());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
