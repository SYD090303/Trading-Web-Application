package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount, UserEntity user);

    Withdrawal processWithdrawal(Long withdrawalId, boolean approved);

    List<Withdrawal> getUserWithdrawalHistory(UserEntity user);

    List<Withdrawal> getUserWithdrawalRequest();
}
