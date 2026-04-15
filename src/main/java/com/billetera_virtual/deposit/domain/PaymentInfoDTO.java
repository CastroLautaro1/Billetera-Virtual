package com.billetera_virtual.deposit.domain;

import java.math.BigDecimal;

public record PaymentInfoDTO(
        boolean isApproved,
        BigDecimal amount,
        String userEmail
) {
}
