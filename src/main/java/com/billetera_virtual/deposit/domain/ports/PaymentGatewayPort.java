package com.billetera_virtual.deposit.domain.ports;

import com.billetera_virtual.deposit.domain.PaymentInfoDTO;
import java.math.BigDecimal;

public interface PaymentGatewayPort {
    PaymentInfoDTO getPaymentInfo(String paymentId);
    String createDepositPreference(BigDecimal amount, String userEmail);
}
