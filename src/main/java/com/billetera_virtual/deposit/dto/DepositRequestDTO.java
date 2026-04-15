package com.billetera_virtual.deposit.dto;

import java.math.BigDecimal;

public record DepositRequestDTO(
        BigDecimal amount,
        String userEmail
) {
}
