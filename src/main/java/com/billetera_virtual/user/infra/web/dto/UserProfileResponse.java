package com.billetera_virtual.user.infra.web.dto;

public record UserProfileResponse(
        String firstname,
        String lastname,
        String email
) {
}
