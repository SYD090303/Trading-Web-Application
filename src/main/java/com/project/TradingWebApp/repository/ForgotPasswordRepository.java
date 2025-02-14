package com.project.TradingWebApp.repository;

import com.project.TradingWebApp.entity.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String> {
    ForgotPassword findByUserId(Long id);
}
