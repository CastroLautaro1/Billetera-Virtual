package com.cuenta_bancaria.cuenta.infra.data.mapper;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import com.cuenta_bancaria.cuenta.infra.data.entity.CuentaEntity;

public class CuentaMapper {

    public static CuentaEntity toEntity(Cuenta domain) {
        return CuentaEntity.builder()
                .id(domain.getId())
                .idUser(domain.getIdUser())
                .monto(domain.getMonto())
                .estado(domain.getEstado())
                .build();
    }


    public static Cuenta toDomain(CuentaEntity entity) {
        return Cuenta.builder()
                .id(entity.getId())
                .idUser(entity.getIdUser())
                .monto(entity.getMonto())
                .estado(entity.getEstado())
                .build();
    }

}
