package com.billetera_virtual.deposit.application;

import com.billetera_virtual.deposit.domain.PaymentInfoDTO;
import com.billetera_virtual.deposit.domain.dto.AccountUpdateResultDTO;
import com.billetera_virtual.deposit.domain.ports.DepositAccountPort;
import com.billetera_virtual.deposit.domain.ports.DepositTransactionPort;
import com.billetera_virtual.deposit.domain.ports.PaymentGatewayPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookDepositUseCase {

    private final PaymentGatewayPort paymentGateway;
    private final DepositAccountPort depositAccount;
    private final DepositTransactionPort depositTransaction;

    @Transactional
    public void processWebHookNotification(String paymentId) {
        // Obtengo la información del pago
        PaymentInfoDTO paymentInfo = paymentGateway.getPaymentInfo(paymentId);

        if (paymentInfo.isApproved()) {

            if(depositTransaction.transactionExists(paymentId)) return;
            // Agrego el deposito al balance de la cuenta
            AccountUpdateResultDTO result = depositAccount.addBalanceByEmail(
                    paymentInfo.userEmail(),
                    paymentInfo.amount()
            );
            // Registro el deposito en una transacción
            depositTransaction.registerDepositTransaction(
                    result.accountId(),
                    paymentInfo.amount(),
                    result.resultingBalance(),
                    paymentId
            );

            System.out.println("✅ PAGO APROBADO: $" + paymentInfo.amount() + '\n' +
                    "Account ID: " + result.accountId());
        } else {
            System.out.println("⚠️ Pago " + paymentId + " no está aprobado todavía.");
        }
    }

}
