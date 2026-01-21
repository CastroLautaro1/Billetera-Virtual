package com.billetera_virtual.account.domain;

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
    private String alias;
    private double balance;
    private boolean status;

}
