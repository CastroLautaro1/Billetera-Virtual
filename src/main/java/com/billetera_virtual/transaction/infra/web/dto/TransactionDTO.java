package com.billetera_virtual.transaction.infra.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotBlank(message = "El alias o CVU de destino es obligatorio")
        String destination,

        @Positive(message = "El monto debe ser un numero positivo")
        @Digits(integer = 10, fraction = 2, message = "El monto no puede tener más de 2 decimales")
        BigDecimal amount,

        @Size(max = 100, message = "El detalle es demasiado largo")
        String details){}
