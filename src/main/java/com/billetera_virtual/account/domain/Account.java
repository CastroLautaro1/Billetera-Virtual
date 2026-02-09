package com.billetera_virtual.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private Long id;
    private Long user_id;
    private String cvu;
    private String alias;
    private BigDecimal balance;
    private boolean status;

}
