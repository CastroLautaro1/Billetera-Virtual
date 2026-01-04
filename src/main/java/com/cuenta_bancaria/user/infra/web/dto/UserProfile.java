package com.cuenta_bancaria.user.infra.web.dto;

public record UserProfile(
        String firstname,
        String lastname,
        String alias,
        String email
) {
}
