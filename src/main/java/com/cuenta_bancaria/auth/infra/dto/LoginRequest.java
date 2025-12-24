package com.cuenta_bancaria.auth.infra.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
