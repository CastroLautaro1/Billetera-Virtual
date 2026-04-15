package com.billetera_virtual.deposit.domain.ports;

import com.billetera_virtual.deposit.domain.dto.AccountUpdateResultDTO;
import java.math.BigDecimal;

public interface DepositAccountPort {
    AccountUpdateResultDTO addBalanceByEmail(String email, BigDecimal amount);
}
