package com.project.TradingWebApp.service.impl;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.project.TradingWebApp.domain.PaymentMethod;
import com.project.TradingWebApp.domain.PaymentOrderStatus;
import com.project.TradingWebApp.entity.PaymentOrder;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.PaymentOrderRepository;
import com.project.TradingWebApp.response.PaymentResponse;
import com.project.TradingWebApp.service.PaymentOrderService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String razorApiKey;

    @Value("${razorpay.api.secret}")
    private String razorSecretKey;

    @Override
    public PaymentOrder createPaymentOrder(UserEntity user, PaymentMethod paymentMethod, Long amount) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentId) {
        return paymentOrderRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment order not found"));
    }

    @Override
    public Boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razprPay = new RazorpayClient(razorApiKey, razorSecretKey);
                Payment payment = razprPay.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(UserEntity user, Long amount) throws RazorpayException {
        Long Amount = amount * 100;
        try{

        RazorpayClient razorpay = new RazorpayClient(razorApiKey, razorSecretKey);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", Amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);
            paymentLinkRequest.put("callback-url","https://localhost:9999/wallet");
            paymentLinkRequest.put("callback-method","get");

            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentUrl(paymentLinkUrl);

            return paymentResponse;
        }
        catch(RazorpayException e){
            System.out.println("Error creating payment link" + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public PaymentResponse createStripePaymentLink(UserEntity user, Long amount, Long orderId) throws StripeException {
        String apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://localhost:9999/wallet?order_id="+orderId)
                .setCancelUrl("https://localhost:9999/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams.LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build())
                                .build())
                        .build())
                .build();
        Session session = Session.create(params);

        System.out.println("Session created: " + session);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentUrl(session.getUrl());
        return paymentResponse;
    }
}
