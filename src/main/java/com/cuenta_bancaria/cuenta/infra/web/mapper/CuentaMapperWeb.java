package com.cuenta_bancaria.cuenta.infra.web.mapper;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.infra.web.dto.CuentaRequest;
import com.cuenta_bancaria.cuenta.infra.web.dto.CuentaUpdateRequest;

public class CuentaMapperWeb {

    public static Cuenta toDomain(CuentaRequest request) {
        return Cuenta.builder()
                .idUser(request.getIdUser())
                .monto(request.getMonto())
                .build();

    }

}
