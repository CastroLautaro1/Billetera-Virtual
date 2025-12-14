package com.cuenta_bancaria.cuenta.infra.data.entity;

import com.cuenta_bancaria.cuenta.domain.Cuenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuenta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuenta")
    private Long id;

    @Column(name = "idUser")
    private Long idUser;

    private double monto;

    @Enumerated(EnumType.STRING)
    private Cuenta.Estado estado;

}
