package com.billetera_virtual.deposit.domain.dto;

import java.math.BigDecimal;

public record AccountUpdateResultDTO(
        Long accountId,
        BigDecimal resultingBalance
) {}
