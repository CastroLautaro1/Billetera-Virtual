package com.cuenta_bancaria.transaction.infra.web.dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String alias;
    private double amount;
    private String details;

}
