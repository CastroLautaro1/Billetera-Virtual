package com.cuenta_bancaria.user.infra.web.dto;

public record UserProfileResponse(
        String firstname,
        String lastname,
        String email
) {
}
