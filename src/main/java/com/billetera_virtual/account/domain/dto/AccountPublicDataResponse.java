package com.billetera_virtual.account.domain.dto;

public record AccountPublicDataResponse (
        Long counterpartyId,
        String firstname,
        String lastname,
        String alias,
        String cvu
){}
