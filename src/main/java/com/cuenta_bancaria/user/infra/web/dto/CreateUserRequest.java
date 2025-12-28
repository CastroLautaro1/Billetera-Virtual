package com.cuenta_bancaria.user.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record CreateUserRequest(
        String firstname,
        String lastname,
        String alias,
        String email,
        String password
){}
