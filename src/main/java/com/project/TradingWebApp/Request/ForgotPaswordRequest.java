package com.project.TradingWebApp.Request;

import com.project.TradingWebApp.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPaswordRequest {
    private String sendTo;
    private VerificationType verificationType;
}
