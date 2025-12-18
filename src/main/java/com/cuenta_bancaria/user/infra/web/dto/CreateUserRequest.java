package com.cuenta_bancaria.user.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String firstname;
    private String lastname;
    private String alias;
    private String email;
    private String password;

}
