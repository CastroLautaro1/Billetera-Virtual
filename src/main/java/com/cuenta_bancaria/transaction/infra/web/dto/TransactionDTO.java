package com.cuenta_bancaria.transaction.infra.web.dto;

import lombok.Data;

@Data
public class TransactionDTO {

    // Cuando agregue security este campo lo quito, por que el ID se obtiene por el token
    // private Long originId;
    private String alias;
    private double amount;
    private String details;

}
