package com.billetera_virtual.transaction.domain.dto;

// Record para recibir datos del módulo Account
public record TransactionAccountInfo(
        String fullname,
        String cvu,
        String alias
) {
}
