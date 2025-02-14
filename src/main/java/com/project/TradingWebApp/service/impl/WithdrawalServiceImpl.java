package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.domain.WithdrawalStatus;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.Withdrawal;
import com.project.TradingWebApp.repository.WithdrawalRepository;
import com.project.TradingWebApp.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, UserEntity user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUser(user);
        withdrawal.setAmount(amount);
        withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal processWithdrawal(Long withdrawalId, boolean approved) {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if (withdrawal.isPresent()) {
            Withdrawal withdrawal1 = withdrawal.get();
            if (approved) {
                withdrawal1.setWithdrawalStatus(WithdrawalStatus.SUCCEED);
            }
            else {
                withdrawal1.setWithdrawalStatus(WithdrawalStatus.REJECTED);
            }
            return withdrawalRepository.save(withdrawal1);
        }
        else{
            throw new RuntimeException("Withdrawal not found");
        }

    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(UserEntity user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getUserWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
