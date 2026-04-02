package com.billetera_virtual.account.domain.dto;

// DTO que combina la informacion de un Usuario y su Cuenta
public record AccountPublicDataResponse (
        Long accountId,
        String firstname,
        String lastname,
        String alias,
        String cvu
){}
