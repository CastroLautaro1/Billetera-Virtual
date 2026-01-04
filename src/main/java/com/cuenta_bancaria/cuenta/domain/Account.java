package com.cuenta_bancaria.cuenta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private Long id;
    private Long user_id;
    private String cvu;
    private double balance;
    private boolean status;

}
