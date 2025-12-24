package com.cuenta_bancaria.auth.infra.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String alias;
    private String email;
    private String password;

}
