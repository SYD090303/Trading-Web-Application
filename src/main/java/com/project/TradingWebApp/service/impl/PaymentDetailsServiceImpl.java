package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.PaymentDetails;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.PaymentDetailsRepository;
import com.project.TradingWebApp.service.PaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, UserEntity user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(UserEntity user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
