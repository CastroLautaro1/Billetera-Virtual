package com.cuenta_bancaria.cuenta.infra.data.entity;

import com.cuenta_bancaria.cuenta.domain.Account;
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
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long id;

    @Column(name = "id_user")
    private Long idUser;

    private double amount;

    private boolean status;

}
