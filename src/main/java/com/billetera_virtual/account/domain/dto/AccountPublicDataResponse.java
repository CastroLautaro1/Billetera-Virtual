package com.billetera_virtual.account.domain.dto;

public record AccountPublicDataResponse (
        String firstname,
        String lastname,
        String alias,
        String cvu
){}
