package com.cuenta_bancaria.cuenta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cuenta {

    private Long id;
    private Long idUser;
    private double monto;
    private boolean estado;

}
