package com.billetera_virtual.deposit.application;

import com.billetera_virtual.deposit.domain.ports.PaymentGatewayPort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CreateDepositLinkUseCase {

    private final PaymentGatewayPort paymentGatewayPort;

    public CreateDepositLinkUseCase(PaymentGatewayPort paymentGatewayPort) {
        this.paymentGatewayPort = paymentGatewayPort;
    }

    public String execute(BigDecimal amount, String userEmail) {
        return paymentGatewayPort.createDepositPreference(amount, userEmail);
    }
}
