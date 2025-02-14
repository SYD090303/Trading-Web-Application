package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.PaymentDetails;
import com.project.TradingWebApp.entity.UserEntity;

public interface PaymentDetailsService {
    PaymentDetails addPaymentDetails(String accountNumber,
                                     String accountHolderName,
                                     String ifsc,
                                     String bankName,
                                     UserEntity user);

    PaymentDetails getUserPaymentDetails(UserEntity user);
}
