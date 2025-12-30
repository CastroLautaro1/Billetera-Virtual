package com.cuenta_bancaria.transaction.infra.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransactionDTO(
        @NotBlank(message = "El alias de destino es obligatorio")
        String alias,

        @Positive(message = "El monto debe ser un numero positivo")
        double amount,

        @Size(max = 100, message = "El detalle es demasiado largo")
        String details){}
